package Impl.Messages.Update

import Classtype.Color
import Globals.GlobalVariables.updateSuccess
import Impl.Messages.TSMSPMessage
import Impl.{STATUS_OK, TSMSPReply}
import Tables.UserTable
import Utils.{DBUtils, IOUtils}
import org.joda.time.DateTime

import scala.util.Try

/**
 * 管理员手动设置，成功返回上传成功
 *
 * @param phone
 * @param color
 */
case class UserUpdateColorMessage(phone: String, color: String) extends TSMSPMessage {
  override def reaction(now: DateTime): Try[TSMSPReply] = Try {
    val newColor = IOUtils.toObject[Color](Some(color)).get.get
    UserTable.closeContact(phone, newColor)
    DBUtils.exec(UserTable.updateColor(phone, newColor).get)
    TSMSPReply(STATUS_OK, updateSuccess)
  }
}
