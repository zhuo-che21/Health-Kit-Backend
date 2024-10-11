package Impl.Messages.Else

import Impl.Messages.TSMSPMessage
import Impl.{STATUS_OK, TSMSPReply}
import Tables.{UserTable, UserTokenTable}
import org.joda.time.DateTime

import scala.util.Try

/**
 * 管理员登录，成功返回Token
 *
 * @param phone
 * @param password
 */
case class ManagerLoginMessage(phone: String, password: String) extends TSMSPMessage {
  override def reaction(now: DateTime): Try[TSMSPReply] = Try {
    if (UserTable.managerLogin(phone, password).get) {
      TSMSPReply(STATUS_OK, UserTokenTable.checkToken(phone).get)
    }
    else {
      throw Exceptions.WrongManagerOrPassword()
    }
  }
}
