package Impl.Messages.Else

import Exceptions.WrongPasswordException
import Impl.Messages.TSMSPMessage
import Impl.{STATUS_OK, TSMSPReply}
import Tables.{UserTable, UserTokenTable}
import Utils.DBUtils
import org.joda.time.DateTime

import scala.util.Try

/**
 * 处理登录请求，登录成功返回Token或登陆失败
 *
 * @param password 密码
 * @param phone    手机号码
 */
case class UserLoginMessage(password: String, phone: String) extends TSMSPMessage {
  override def reaction(now: DateTime): Try[TSMSPReply] = Try {
    if (DBUtils.exec(UserTable.checkPassword(phone, password).get)) TSMSPReply(STATUS_OK, UserTokenTable.checkToken(phone).get)
    else throw WrongPasswordException()
  }
}
