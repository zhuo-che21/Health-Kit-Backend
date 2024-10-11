package Impl.Messages.Else

import Exceptions.UserAlreadyExistsException
import Impl.Messages.TSMSPMessage
import Impl.{STATUS_OK, TSMSPReply}
import Tables.{UserTable, UserTokenTable, VaccTable}
import Utils.DBUtils
import org.joda.time.DateTime

import scala.util.Try

/**
 * 根据注册信息注册用户，返回Token或注册失败
 *
 * @param name     姓名
 * @param password 密码
 * @param phone    手机号码
 * @param ident    身份证号
 */
case class UserRegisterMessage(name: String, password: String, phone: String, ident: String) extends TSMSPMessage {
  override def reaction(now: DateTime): Try[TSMSPReply] = Try {
    if (DBUtils.exec(UserTable.checkUserExistsWithIdent(ident).get)) throw UserAlreadyExistsException()
    else if (DBUtils.exec(UserTable.checkUserExistsWithPhone(phone).get)) throw UserAlreadyExistsException()
    else {
      DBUtils.exec(UserTable.addUser(name, password, ident, phone).get.andThen(UserTokenTable.addRow(phone).get).andThen(VaccTable.addVacc(phone).get))
      TSMSPReply(STATUS_OK, UserTokenTable.checkToken(phone).get)
    }
  }
}
