package Impl.Messages.Del

import Impl.Messages.TSMSPMessage
import Impl.{STATUS_OK, TSMSPReply}
import Tables.{AcidTestTable, UserTokenTable}
import Utils.DBUtils
import org.joda.time.DateTime

import scala.util.Try

/**
 * 删除14天前所有记录
 *
 * @param userToken
 */
case class DelFourteenAcidMessage(userToken: String) extends TSMSPMessage {
  override def reaction(now: DateTime): Try[TSMSPReply] = Try {
    val phone = UserTokenTable.checkPhone(userToken).get
    DBUtils.exec(AcidTestTable.delFourteen(phone).get)
    TSMSPReply(STATUS_OK, Globals.GlobalVariables.SuccessDel)
  }
}
