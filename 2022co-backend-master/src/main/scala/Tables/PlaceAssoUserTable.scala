package Tables

import Globals.GlobalVariables
import Tables.UserTraceTable.userTraceTable
import Utils.DBUtils
import slick.jdbc.PostgresProfile.api._
import slick.lifted.{ProvenShape, Tag}
import org.joda.time.DateTime
import slick.ast.ColumnOption

import scala.util.Try

case class PlaAssoUserRow(
                         ID: String,
                         phone: String,
                         time: Long,
                         )

/**
 * 记录有什么人经过了某个地点（ID）
 */
class PlaceAssoUserTable(tag: Tag) extends Table[PlaAssoUserRow](tag, GlobalVariables.mainSchema, "PlaceAssoUser"){
  def ID = column[String]("id")
  def phone = column[String]("phone")
  def time = column[Long]("time")
  def * = (ID, phone, time).mapTo[PlaAssoUserRow]
}

object PlaceAssoUserTable {
  val placeAssoUserTable = TableQuery[PlaceAssoUserTable]

  /**
   * check 某个用户是否经过 place
   * @param ID
   * @param phone
   * @return
   */
  def checkPasser(ID: String, phone: String): Try[DBIO[Boolean]] =
    Try((placeAssoUserTable.filter(p => p.ID === ID && p.phone === phone).exists.result))

  /**
   * 将14天前的记录删除
   * @param ID
   * @param phone
   * @return
   */
  def delRecordForteen(ID: String): Try[DBIO[Int]]=
    Try{
      val forteenDayAgo = DateTime.now().getMillis-GlobalVariables.forteenDayInMinisecond
      placeAssoUserTable.filter(u => u.ID === ID && u.time <= forteenDayAgo).delete
    }

}
