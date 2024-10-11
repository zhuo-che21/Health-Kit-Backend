package Impl.Messages.Get

import Impl.Messages.TSMSPMessage
import Impl.{STATUS_OK, TSMSPReply}
import Tables.{RelativeTable, UserTable, UserTokenTable}
import Utils.{DBUtils, IOUtils}
import org.joda.time.DateTime

import scala.util.Try

/**
 * 通过电话查询关联用户的信息
 *
 * @param userToken
 * @param relPhone
 */
case class QueryRelativeInfoMessage(userToken: String, relPhone: String) extends TSMSPMessage {
  override def reaction(now: DateTime): Try[TSMSPReply] = Try {
    val phone = UserTokenTable.checkPhone(userToken).get
    if (DBUtils.exec(RelativeTable.checkRelByPhone(phone, relPhone).get)) {
      TSMSPReply(STATUS_OK, IOUtils.serialize(DBUtils.exec(UserTable.returnInfo(relPhone).get)).get)
    }
    else throw Exceptions.RelativeNotExists()
  }
}
