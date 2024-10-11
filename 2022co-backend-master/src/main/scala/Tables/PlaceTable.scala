package Tables

import Classtype.{Place, Rate}
import Exceptions.{TokenNotExistsException, UserNotExistsException}
import Globals.GlobalVariables
import Utils.{DBUtils, StringUtils}
import slick.jdbc.PostgresProfile.api._
import slick.lifted.{ProvenShape, Tag}

import scala.util.Try
import Tables.PlaceAssoUserTable
import Tables.UserTraceTable
import Utils.CustomColumnTypes.jacksonSerializableType
import slick.ast.ColumnOption.PrimaryKey
/**
 *
 * @param district 区
 * @param street 街道
 * @param num 门牌号
 * @param rate 风险级别
 * @param ID 地点一一对应的id
 */

case class PlaceRow(
                   placeName: Place,
                   rate : Rate ,
                   ID : String,
                   )

class PlaceTable(tag : Tag) extends Table[PlaceRow](tag, GlobalVariables.mainSchema, "place"){
  def placeName = column[Place]("place_name")
  def rate = column[Rate]("rate")
  def ID = column[String]("ID")
  def * = (placeName, rate, ID).mapTo[PlaceRow]
}

object PlaceTable {
  val placeTable = TableQuery[PlaceTable]

  def addPlace(placeName: Place, rate: Rate): Try[DBIO[Int]] =Try{
    var newID: String = StringUtils.randomString(30)
    while (DBUtils.exec(placeTable.filter(_.ID === newID).size.result) > 0)
      newID = StringUtils.randomString(30)
    placeTable += PlaceRow(placeName, rate, ID = newID)
  }

  /**
   * 通过ID找Place
   * @param ID
   * @return
   */
  def transIDtoPlaName(ID: String): Try[DBIO[Place]] = Try{
    placeTable.filter(_.ID === ID).map(_.placeName).result.head
  }

  /**
   * 类上
   */
  def transPlaNametoID(place: Place): Try[DBIO[String]] =
    Try((placeTable.filter(p => p.placeName === place).map(_.ID).result.head))

  def returnRate(ID: String): Try[DBIO[Rate]]=
    Try((placeTable.filter(_.ID === ID).map(_.rate).result.head))

  /**
   * （管理员）通过地点名改rate
   */
  def changeRateWithName(placeName: Place, newRate: Rate): Try[DBIO[Int]] =
    Try(placeTable.filter(u => u.placeName === placeName).map(_.rate).update(newRate))

  /**
   * 通过ID改rate
   */
  def changeRateWithID(ID : String, newRate: Rate): Try[DBIO[Int]] =
    Try(placeTable.filter(_.ID === ID).map(_.rate).update(newRate))

  def checkPlaceWithID(ID: String): Try[DBIO[Boolean]] =
    Try((placeTable.filter(_.ID === ID).exists.result))

  def checkPlaceWithName(placeName: Place): Try[DBIO[Boolean]] =
    Try((placeTable.filter(p => p.placeName === placeName).exists.result))
  /**
   * 同上
   */
  def delPlaceWithID(ID: String): Try[DBIO[Int]]={
    Try((placeTable.filter(_.ID === ID).delete))
    Try((PlaceAssoUserTable.placeAssoUserTable.filter(_.ID === ID).delete))
    Try((UserTraceTable.userTraceTable.filter(_.traceID === ID).delete))
  }
}