package Impl.Messages.Else

import Globals.GlobalVariables.managerSetSuccess
import Impl.Messages.TSMSPMessage
import Impl.{STATUS_OK, TSMSPReply}
import Tables.UserTable
import Utils.DBUtils
import org.joda.time.DateTime

import scala.util.Try

/**
 * 管理员用户将另一个用户设置成管理员
 *
 * @param phone 要设置的用户
 */
case class GiveManagerMessage(phone: String) extends TSMSPMessage {
  override def reaction(now: DateTime): Try[TSMSPReply] = Try {
    if (DBUtils.exec(UserTable.checkUserExistsWithPhone(phone).get)) {
      DBUtils.exec(UserTable.transToManager(phone).get)
      TSMSPReply(STATUS_OK, managerSetSuccess)
    }
    else throw Exceptions.UserNotExistsException()
  }
}
