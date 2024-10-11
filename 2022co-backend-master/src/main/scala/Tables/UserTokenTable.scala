package Tables


import Exceptions.{TokenNotExistsException, UserNotExistsException}
import Globals.GlobalVariables
import Utils.{DBUtils, StringUtils}
import org.joda.time.DateTime
import slick.jdbc.PostgresProfile.api._
import slick.lifted.{ProvenShape, Tag}

import scala.util.Try

/**
 *
 * @param phone 用户phone
 * @param token 通讯用的“密令”
 * @param refreshTime 密令“有效时间”为refreshtime+2hours以内
 */
case class UserTokenRow(
                          phone : String,
                          token : String,
                          refreshTime : Long,
                         )

class UserTokenTable(tag : Tag) extends Table[UserTokenRow](tag, GlobalVariables.mainSchema, "user_token") {
  def phone = column[String]("phone", O.PrimaryKey)
  def token = column[String]("token")
  def refreshTime = column[Long]("refresh_time")
  def * = (phone, token, refreshTime).mapTo[UserTokenRow]
}

object UserTokenTable {
  val userTokenTable = TableQuery[UserTokenTable]

  def addRow(phone : String) : Try[DBIO[Int]] = Try {
    var newToken: String = StringUtils.randomString(30)
    while (DBUtils.exec(userTokenTable.filter(_.token === newToken).size.result) > 0)
      newToken = StringUtils.randomString(30)
    userTokenTable += UserTokenRow(phone, newToken, DateTime.now().minusYears(2).getMillis)
  }

  /**
   * 检查有没有超过两个小时没有操作
   * @param phone
   * @return 如果超过两小时 更新token
   */
  def checkToken(phone : String) : Try[String] = Try {
    val nowTokenPair = DBUtils.exec(userTokenTable.filter(ut => ut.phone === phone).map(ut => (ut.token, ut.refreshTime)).result.headOption).getOrElse(throw UserNotExistsException())
    if (nowTokenPair._2 >= DateTime.now().minusHours(2).getMillis) {
      DBUtils.exec(userTokenTable.filter(ut => ut.phone === phone).map(_.refreshTime).update(DateTime.now().getMillis))
      nowTokenPair._1
    } else {
      var newToken : String = StringUtils.randomString(30)
      while (DBUtils.exec(userTokenTable.filter(_.token === newToken).size.result) > 0)
        newToken = StringUtils.randomString(30)
      DBUtils.exec(userTokenTable.filter(_.phone === phone).map(ut => (ut.token, ut.refreshTime)).update((newToken, DateTime.now().getMillis)))//一起更新
      newToken
    }
  }

  /**
   * 通过token找phone
   * @param token 前端传的加密信息
   * @return
   */
  def checkPhone(token : String) : Try[String] = Try {
    DBUtils.exec(userTokenTable.filter(ut => ut.token === token && ut.refreshTime >= DateTime.now().minusHours(2).getMillis).map(_.phone).result.headOption).getOrElse(
      throw TokenNotExistsException()
    )
  }
}