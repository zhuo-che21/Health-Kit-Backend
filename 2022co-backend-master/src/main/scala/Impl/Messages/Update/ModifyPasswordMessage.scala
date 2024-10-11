package Impl.Messages.Update

import Exceptions.{SamePassword, UserNotExistsException, WrongPasswordException}
import Impl.Messages.TSMSPMessage
import Impl.{STATUS_OK, TSMSPReply}
import Tables.{UserTable, UserTokenTable}
import Utils.DBUtils
import org.joda.time.DateTime

import scala.util.Try

/**
 * 成功返回新的Token
 *
 * @param userToken
 * @param oldPassword
 * @param newPassword
 */
case class ModifyPasswordMessage(userToken: String, oldPassword: String, newPassword: String) extends TSMSPMessage {
  override def reaction(now: DateTime): Try[TSMSPReply] = Try {
    val phone = UserTokenTable.checkPhone(userToken).get
    if (DBUtils.exec(UserTable.checkUserExistsWithPhone(phone).get)) {
      if (DBUtils.exec(UserTable.checkPassword(phone, oldPassword).get)) {
        if (oldPassword != newPassword) {
          DBUtils.exec(UserTable.changePassword(phone, oldPassword, newPassword).get)
          TSMSPReply(STATUS_OK, UserTokenTable.checkToken(phone).get)
          //TSMSPReply(STATUS_OK, "假成功！")
        }
        else throw SamePassword()
      }
      else throw WrongPasswordException()
    }
    else throw UserNotExistsException()
  }
}
