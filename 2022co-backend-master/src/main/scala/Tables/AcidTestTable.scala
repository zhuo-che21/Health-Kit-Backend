package Tables

import Classtype.ResultOfAcid
import Globals.GlobalVariables
import Impl.JacksonSerializable
import Utils.CustomColumnTypes.jacksonSerializableType
import Utils.DBUtils
import org.joda.time.DateTime
import slick.jdbc.PostgresProfile.api._
import slick.lifted.{ProvenShape, Tag}
import slick.ast.ColumnOption.{PrimaryKey, Unique}

import scala.util.Try

/**
 * 核酸记录 （含历史记录）
 * @param phone
 * @param time 测试时间
 * @param result 结果
 * @param agency 检测机构
 */
case class AcidTestRow(
                        time: Long,
                        phone: String,
                        result: ResultOfAcid,
                        agency: String,
                      ) extends JacksonSerializable

class AcidTestTable(tag: Tag) extends Table[AcidTestRow](tag, GlobalVariables.mainSchema, "acid_test_table"){
  def phone = column[String]("phone")
  def time = column[Long]("last_time")
  def result = column[ResultOfAcid]("result")
  def agency = column[String]("agency")
  def * = (time, phone, result, agency).mapTo[AcidTestRow]
}

object AcidTestTable{
  val acidTestTable = TableQuery[AcidTestTable]

  def addRecord(phone: String, resul: ResultOfAcid, agency: String):Try[DBIO[Int]] =
    Try(acidTestTable += AcidTestRow(time = DateTime.now().getMillis,phone = phone,  result = resul, agency = agency))

  /**
   * check用户是否记有核算记录
   * @param phone
   * @return 如果有返回true
   */
  def checkEmpty(phone: String): Try[DBIO[Boolean]] =
    Try(acidTestTable.filter(_.phone === phone).exists.result)
  def returnAll(phone: String): Try[List[AcidTestRow]] =
    Try(DBUtils.exec(acidTestTable.filter(_.phone === phone).sortBy(_.time.desc).result).toList)
  def returnLastAcidTest(phone: String): Try[DBIO[AcidTestRow]] =
    Try(acidTestTable.filter(_.phone === phone).sortBy(_.time.desc).result.head)

  def delFourteen(phone: String):Try[DBIO[Int]] = Try {
    val fourteenDayAgo = DateTime.now().getMillis - GlobalVariables.forteenDayInMinisecond
    (acidTestTable.filter(u=> u.phone === phone &&u.time <= fourteenDayAgo ).delete)
  }
}