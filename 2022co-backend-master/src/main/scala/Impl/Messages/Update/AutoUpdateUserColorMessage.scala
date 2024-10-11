package Impl.Messages.Update

import Impl.Messages.TSMSPMessage
import Impl.{STATUS_OK, TSMSPReply}
import Tables.UserTable
import Utils.DBUtils
import org.joda.time.DateTime

import scala.util.Try

/**
 * 自动更新用户Color信息
 * @param phone
 */
case class AutoUpdateUserColorMessage(phone : String) extends TSMSPMessage {
  override def reaction(now: DateTime): Try[TSMSPReply] = Try{
    if(DBUtils.exec(UserTable.checkUserExistsWithPhone(phone).get)) {
      UserTable.checkColor(phone)
      TSMSPReply(STATUS_OK, Globals.GlobalVariables.updateSuccess)
    }
    else throw Exceptions.UserNotExistsException()
  }
}
