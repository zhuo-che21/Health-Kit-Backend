package Impl.Messages.Get

import Impl.Messages.TSMSPMessage
import Impl.{STATUS_OK, TSMSPReply}
import Tables.{AcidTestTable, UserTokenTable}
import Utils.{DBUtils, IOUtils}
import org.joda.time.DateTime

import scala.util.Try

/**
 * 返回用户的所有核酸记录
 *
 * @param userToken
 */
case class QueryAllAcidMessage(userToken: String) extends TSMSPMessage {
  override def reaction(now: DateTime): Try[TSMSPReply] = Try {
    val phone = UserTokenTable.checkPhone(userToken).get
    if (DBUtils.exec(AcidTestTable.checkEmpty(phone).get)) {
      TSMSPReply(STATUS_OK, IOUtils.serialize(AcidTestTable.returnAll(phone).get).get)
    }
    else throw Exceptions.AcidTestEmpty()
  }
}
