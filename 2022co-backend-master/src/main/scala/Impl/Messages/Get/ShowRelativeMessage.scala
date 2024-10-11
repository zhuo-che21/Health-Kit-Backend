package Impl.Messages.Get

import Impl.Messages.TSMSPMessage
import Impl.{STATUS_ERROR, STATUS_OK, TSMSPReply}
import Tables.{RelativeTable, UserTokenTable}
import Utils.{DBUtils, IOUtils}
import org.joda.time.DateTime

import scala.util.Try

/**
 * 显示所以关联用户的姓名
 *
 * @param userToken
 */
case class ShowRelativeMessage(userToken: String) extends TSMSPMessage {
  override def reaction(now: DateTime): Try[TSMSPReply] = Try {
    val phone = UserTokenTable.checkPhone(userToken).get
    if (DBUtils.exec(RelativeTable.checkExist(phone).get)) {
      TSMSPReply(STATUS_OK, IOUtils.serialize(RelativeTable.showRel(phone).get).get)
    }
    else {
      TSMSPReply(STATUS_OK, "")
    }
  }
}
