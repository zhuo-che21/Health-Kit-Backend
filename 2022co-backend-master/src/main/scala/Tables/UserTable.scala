package Tables

import Classtype.{Color, Rate, Vaccine}
import Globals.GlobalVariables
import Utils.DBUtils
import slick.jdbc.PostgresProfile.api._
import slick.lifted.{ProvenShape, Tag}
import Tables.UserTraceTable
import Tables.UserTokenTable
import Tables.PlaceAssoUserTable
import Utils.CustomColumnTypes.jacksonSerializableType
import org.joda.time.DateTime
import slick.ast.ColumnOption.Unique

import scala.util.Try

/**
 * 用户基本信息
 * @param name
 * @param password
 * @param ident 身份证
 * @param phone
 */
case class UserRow(
                    name : String,
                    password : String,
                    ident: String,
                    phone: String,
                    color: Color,
                    isManager: Boolean,
                  )

class UserTable(tag : Tag) extends Table[UserRow](tag, GlobalVariables.mainSchema, "user") {
  def name = column[String]("user_name")
  def password = column[String]("password")
  def ident = column[String]("identity")
  def phone = column[String]("phone", Unique)
  def color = column[Color]("color")
  def isManager = column[Boolean]("isManager")
  def * = (name, password, ident, phone, color, isManager).mapTo[UserRow]
}

object UserTable {
  val userTable = TableQuery[UserTable]

  def addUser(name: String, password: String, ident: String, phone: String): Try[DBIO[Int]] =
    Try{
        val col = Color.getType("yellow")
        userTable += UserRow(name, password, ident, phone, col, false)
    }

  def addManager(name: String, password: String, ident: String, phone: String): Try[DBIO[Int]] =
    Try(userTable += UserRow(name, password, ident, phone, color = Color.getType("green"), isManager = true))

  /**
   * 判断是不是管理员
   * @param phone
   * @return
   */
  def checkManager(phone: String):Try[DBIO[Boolean]] =
    Try(userTable.filter(_.phone === phone).map(_.isManager).result.head)

  /**
   * 管理员登录
   * @param phone
   * @param password
   * @return
   */
  def managerLogin(phone: String, password: String):Try[Boolean] = Try{
    if (phone == GlobalVariables.metaManager && password == GlobalVariables.metaPassword){
      true
    }
    else if(DBUtils.exec(userTable.filter(u => u.phone === phone && u.password === password && u.isManager === true).exists.result)){
      true
    }
    else{
      false
    }
  }
  def transToManager(phone: String): Try[DBIO[Int]] =
    Try(userTable.filter(_.phone === phone).map(_.isManager).update(true))

  /**
   * （管理员操作）改user的color
   * @param phone
   * @param newColor
   * @return
   */
  def updateColor(phone: String, newColor: Color): Try[DBIO[Int]] =
    Try(userTable.filter(_.phone === phone).map(_.color).update(newColor))

  /**
   *通过查找trace的rate （实时）更新用户的健康状态
   * @param phone
   * @return
   */
  def checkColor(phone: String): Try[Int] = Try{
    if (DBUtils.exec(UserTraceTable.userTraceTable.filter(u => u.phone === phone &&
      u.rate === Rate.getType("high")).exists.result)){
      DBUtils.exec(updateColor(phone, Color.getType("red")).get)
    }
    else if(DBUtils.exec(UserTraceTable.userTraceTable.filter(u => u.phone === phone &&
      u.rate === Rate.getType("middle")).exists.result)){
      DBUtils.exec(updateColor(phone, Color.getType("yellow")).get)
    }
    else {
      DBUtils.exec(updateColor(phone, Color.getType("green")).get)
    }
  }

  /**
   *检查名字与电话对不对的上
   * @param phone
   * @param name
   * @return
   */
  def checkPhoneForName(phone: String, name: String): Try[DBIO[Boolean]] = Try(
    userTable.filter(u => u.phone === phone && u.name === name).exists.result
  )

  /**
   * 当有用户updateColor时 调用此函数将该用户的密接改色
   * @param phone
   * @param newColor
   * @return
   */
  def closeContact(phone: String, newColor: Color): Try[Int] = Try{
    val traceID_list = DBUtils.exec(UserTraceTable.userTraceTable.filter(_.phone === phone).map(_.traceID).result).toList
    val close_contact_list =  DBUtils.exec(UserTraceTable.userTraceTable.filter(_.traceID inSet (traceID_list)).map(_.phone).result).toList
    DBUtils.exec(userTable.filter(u => u.phone inSetBind(close_contact_list)).filter(_.color =!= Color.getType("green")).map(_.color).update(newColor))
  }
  /**
   * 返回信息
   * @param phone
   * @return
   */
  def returnInfo(phone: String): Try[DBIO[UserRow]] =
    Try(userTable.filter(_.phone === phone).result.head)
  def returnColor(phone: String): Try[DBIO[Color]]=
    Try(userTable.filter(_.phone === phone).map(_.color).result.head)
  def checkUserExistsWithIdent(ident: String): Try[DBIO[Boolean]] =
    Try((userTable.filter(_.ident === ident).exists.result))

  def checkUserExistsWithPhone(phone: String): Try[DBIO[Boolean]] =
    Try((userTable.filter(_.phone === phone).exists.result))
  def checkPassword(phone: String, password: String): Try[DBIO[Boolean]] =
    Try((userTable.filter(u => u.phone === phone && u.password === password).exists.result))

  def delUser(phone : String): Try[Int] = {
    Try(DBUtils.exec(userTable.filter(_.phone === phone).delete))
    Try(DBUtils.exec(UserTraceTable.userTraceTable.filter(_.phone === phone).delete))
    Try(DBUtils.exec(UserTokenTable.userTokenTable.filter(_.phone === phone).delete))
    Try(DBUtils.exec(PlaceAssoUserTable.placeAssoUserTable.filter(_.phone === phone).delete))
    Try(DBUtils.exec(VaccTable.vaccTable.filter(_.phone === phone).delete))
  }
  def changePassword(phone: String, oldPassword: String, newPassword : String): Try[DBIO[Int]] =
    Try(userTable.filter(u => u.phone === phone && u.password === oldPassword ).map(_.password).update(newPassword))

}