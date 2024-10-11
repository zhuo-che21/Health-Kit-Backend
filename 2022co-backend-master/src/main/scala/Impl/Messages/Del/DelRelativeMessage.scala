package Impl.Messages.Del

import Impl.Messages.TSMSPMessage
import Impl.{STATUS_OK, TSMSPReply}
import Tables.{RelativeTable, UserTokenTable}
import Utils.DBUtils
import org.joda.time.DateTime

import scala.util.Try

/**
 * 删除指定关联用户
 *
 * @param userToken
 * @param relPhone
 */
case class DelRelativeMessage(userToken: String, relPhone: String) extends TSMSPMessage {
  override def reaction(now: DateTime): Try[TSMSPReply] = Try {
    val phone = UserTokenTable.checkPhone(userToken).get
    if (DBUtils.exec(RelativeTable.checkRelByPhone(phone, relPhone).get)) {
      DBUtils.exec(RelativeTable.delRelPhone(phone, relPhone).get)
      TSMSPReply(STATUS_OK, UserTokenTable.checkToken(phone).get)
    }
    else throw Exceptions.RelativeNotExists()
  }
}
