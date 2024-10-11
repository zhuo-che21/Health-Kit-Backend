package Tables

import Tables.UserTable
import Globals.GlobalVariables
import Utils.DBUtils
import slick.jdbc.PostgresProfile.api._
import slick.lifted.{ProvenShape, Tag}
import slick.ast.ColumnOption.Unique
import slick.ast.ColumnOption.PrimaryKey
import scala.util.Try

/**
 * 亲属信息
 * @param phone （自己）的phone
 * @param relName 亲属name
 * @param relPhone 亲属phone
 */
case class RelativeRow(
                      phone: String,
                      relName: String,
                      relPhone: String,
                      )

class RelativeTable(tag : Tag) extends Table[RelativeRow](tag, GlobalVariables.mainSchema, "relative"){
  def phone = column[String]("phone")
  def relName = column[String]("relative_name")
  def relPhone = column[String]("relative_phone")
  def * = (phone, relName, relPhone).mapTo[RelativeRow]
}

object RelativeTable{
  val relativeTable = TableQuery[RelativeTable]

  /**
   *通过passward 检验用户是否有权将relPhone对应的用户加为rel
   * @param relPhone
   * @param password
   * @return
   */
  def getAuthority(relPhone: String, password: String):Try[DBIO[Boolean]] =
    Try(UserTable.userTable.filter(u => u.password === password && u.phone === relPhone).exists.result)
  def addRel(phone: String, relName: String, relPhone: String): Try[DBIO[Int]] =
    Try(relativeTable += RelativeRow(phone, relName, relPhone))

  def checkRelByPhone(phone: String, relPhone: String):Try[DBIO[Boolean]] =
    Try(relativeTable.filter(r => r.phone === phone && r.relPhone=== relPhone) .exists.result)

  def checkExist(phone: String): Try[DBIO[Boolean]] =
    Try(relativeTable.filter(_.phone === phone).exists.result)

  def showRel(phone: String):Try[List[RelativeRow]] =
    Try(DBUtils.exec(relativeTable.filter(_.phone === phone).result).toList)

  def changeRelName(phone: String, oldName: String, relPhone: String, newName: String):Try[DBIO[Int]] =
    Try(relativeTable.filter(r => r.phone === phone && r.relName === oldName && r.relPhone === relPhone).map(_.relName).update(newName))

  def changeRelPhone(phone: String, oldPhone: String, newPhone: String): Try[DBIO[Int]] =
    Try(relativeTable.filter(r => r.phone === phone && r.relPhone === oldPhone).map(_.relPhone).update(newPhone))

  def delRelName(phone: String, relName: String): Try[DBIO[Int]] =
    Try(relativeTable.filter(r => r.phone === phone && r.relName === relName).delete)

  def delRelPhone(phone: String, relPhone: String): Try[DBIO[Int]] =
    Try(relativeTable.filter(r => r.phone === phone && r.relPhone === relPhone).delete)

}