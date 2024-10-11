package Impl.Messages.Del

import Exceptions.UserNotExistsException
import Globals.GlobalVariables.SuccessDel
import Impl.Messages.TSMSPMessage
import Impl.{STATUS_OK, TSMSPReply}
import Tables.UserTable
import Utils.DBUtils
import org.joda.time.DateTime

import scala.util.Try

/**
 * 通过手机号码注销用户，返回是否注销成功
 *
 * @param phone
 */
case class DelUserMessage(phone: String) extends TSMSPMessage {
  override def reaction(now: DateTime): Try[TSMSPReply] = Try {
    //val WhetherUser = UserTable.checkUserExists(userName).get
    //if (!WhetherUser) TSMSPReply(STATUS_OK, "假成功！")
    if (DBUtils.exec(UserTable.checkUserExistsWithPhone(phone).get)) {
      UserTable.delUser(phone).get
      TSMSPReply(STATUS_OK, SuccessDel)
    }
    else throw UserNotExistsException()
  }
}
