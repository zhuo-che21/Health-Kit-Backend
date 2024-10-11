package Tables


import Classtype.{Place, Rate}
import Globals.GlobalVariables
import Tables.PlaceAssoUserTable.placeAssoUserTable
import Tables.UserTable.userTable
import Utils.CustomColumnTypes.jacksonSerializableType
import Utils.DBUtils
import org.joda.time.DateTime
import org.joda.time.Days
import slick.jdbc.PostgresProfile.api._
import slick.lifted.{ProvenShape, Tag}
import slick.ast.ColumnOption.{PrimaryKey, Unique}

import scala.util.Try

case class UserTraceRow(
                        phone : String ,
                        traceID : String,
                        time : Long,
                        placeName: Place,
                        rate: Rate,
                       )

class UserTraceTable(tag : Tag) extends Table[UserTraceRow](tag, GlobalVariables.mainSchema, "user_trace") {
  def phone = column[String]("user_phone")
  def traceID = column[String]("trace")
  def time = column[Long]("time")
  def placeName = column[Place]("place_name")
  def rate = column[Rate]("rate")
  def * = (phone, traceID, time, placeName, rate).mapTo[UserTraceRow]

}

object UserTraceTable {
  val userTraceTable = TableQuery[UserTraceTable]

  def addTrace(phone: String, traceID : String, placeName: Place, rate: Rate) : Try[Int] =
    Try {
      DBUtils.exec(userTraceTable += UserTraceRow(phone, traceID, time = DateTime.now().getMillis, placeName, rate))
      DBUtils.exec(placeAssoUserTable += PlaAssoUserRow(traceID, phone, time = DateTime.now().getMillis))
    }
  def checkTrace(phone: String, startTime : Long, endTime : Long) : Try[List[UserTraceRow]] = Try {
    DBUtils.exec(userTraceTable.filter(ut => ut.phone === phone && ut.time<= endTime
      && ut.time >= startTime).sortBy(_.time.desc).result).toList
  }

  def checkEmpty(phone: String): Try[DBIO[Boolean]] =
    Try((userTraceTable.filter(_.phone === phone).exists.result))
  def delEarlistTrace(phone: String) : Try[DBIO[Int]] =
    Try(userTraceTable.filter(u => u.time === (userTraceTable.filter(_.phone === phone).map(_.time).min)).delete)

  /**
   * 删除14天前的路径
   * @param phone
   * @return
   */
  def delTraceForteen(phone: String) : Try[Int]=
    Try{
      val forteenDayAgo = DateTime.now().getMillis-GlobalVariables.forteenDayInMinisecond
      DBUtils.exec(userTraceTable.filter(u => u.phone === phone && u.time <= forteenDayAgo).delete)
      DBUtils.exec(placeAssoUserTable.filter(u => u.phone === phone && u.time <= forteenDayAgo).delete)
    }
  def delTrace(phone: String, placeID: String): Try[DBIO[Int]] = {
    Try (userTraceTable.filter(u => u.phone === phone && u.traceID === placeID).delete)
  }
  def searchTrace(phone: String, placeID: String) : Try[DBIO[Boolean]] =
    Try((userTraceTable.filter(u => u.phone === phone && u.traceID===placeID).exists.result))

  /**
   * 返回经过一个地方的人
   * @param placeID
   * @return List[UserRow]
   */
  def returePasser(placeID: String): Try[List[UserRow]] = Try{
    val passer_phone_list = DBUtils.exec(userTraceTable.filter(_.traceID === placeID).map(_.phone).result).toList
    DBUtils.exec(userTable.filter(_.phone inSet(passer_phone_list)).result).toList
  }
  def modifyTrace(phone: String, oldPlaceID: String, newPlaceID: String) : Try[DBIO[Int]] =
    Try(userTraceTable.filter(u => u.traceID === oldPlaceID && u.phone === phone).map(_.traceID).update(newPlaceID))

}
