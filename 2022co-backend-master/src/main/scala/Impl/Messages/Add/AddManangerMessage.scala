package Impl.Messages.Add

import Exceptions.UserAlreadyExistsException
import Impl.Messages.TSMSPMessage
import Impl.{STATUS_OK, TSMSPReply}
import Tables.{UserTable, UserTokenTable}
import Utils.DBUtils
import org.joda.time.DateTime

import scala.util.Try

/**
 * 创建管理者用户，成功返回Token
 *
 * @param name
 * @param password
 * @param phone
 * @param ident
 */
case class AddManangerMessage(name: String, password: String, phone: String, ident: String) extends TSMSPMessage {
  override def reaction(now: DateTime): Try[TSMSPReply] = Try {
    if (DBUtils.exec(UserTable.checkUserExistsWithIdent(ident).get)) throw UserAlreadyExistsException()
    else if (DBUtils.exec(UserTable.checkUserExistsWithPhone(phone).get)) throw UserAlreadyExistsException()
    else {
      DBUtils.exec(UserTable.addManager(name, password, ident, phone).get.andThen(UserTokenTable.addRow(phone).get))
      TSMSPReply(STATUS_OK, UserTokenTable.checkToken(phone).get)
    }
  }
}
