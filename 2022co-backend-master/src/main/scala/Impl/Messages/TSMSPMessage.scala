package Impl.Messages

import Impl.Messages.Add.{AcceptRequestMessage, AddAcidTestMessage, AddManangerMessage, AddPlaceMessage, AddRequestMessage, RejectRequestMessage}
import Impl.Messages.Del.{DelEarliestTraceMessage, DelFourteenAcidMessage, DelPlaceMessage, DelRecordFourteenMessage, DelRelativeMessage, DelRequestMessage, DelTraceFourteenMessage, DelTraceMessage, DelUserMessage}
import Impl.Messages.Else.{GiveManagerMessage, ManagerLoginMessage, SearchTraceMessage, UserLoginMessage, UserRegisterMessage}
import Impl.Messages.Get.{GetBoostVaccMessage, GetFirstVaccMessage, GetPasserMessage, GetSecondVaccMessage, GetThirdVaccMessage, QueryAllAcidMessage, QueryLastAcidTestMessage, QueryPlaceIDMessage, QueryRelativeInfoMessage, QueryUserInfoMessage, ShowRelativeMessage, ShowRequestedMessage, ShowRequestingMessage, UserGetTraceMessage}
import Impl.Messages.Update.{AutoUpdateUserColorMessage, ChangeRateWithIDMessage, ChangeRateWithNameMessage, ModifyPasswordMessage, ModifyTraceMessage, UpdateBoostVaccMessage, UpdateFirstVaccMessage, UpdateSecondVaccMessage, UpdateThirdVaccMessage, UserUpdateColorMessage, UserUpdateTraceMessage}
import Impl.{JacksonSerializable, STATUS_ERROR, TSMSPReply}
import com.fasterxml.jackson.annotation.{JsonSubTypes, JsonTypeInfo}
import org.joda.time.DateTime

import scala.util.{Failure, Success, Try}

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "type")
@JsonSubTypes(
  Array(
    new JsonSubTypes.Type(value = classOf[UserGetTraceMessage], name = "UserGetTraceMessage"),
    new JsonSubTypes.Type(value = classOf[UserLoginMessage], name = "UserLoginMessage"),
    new JsonSubTypes.Type(value = classOf[UserRegisterMessage], name = "UserRegisterMessage"),
    new JsonSubTypes.Type(value = classOf[UserUpdateTraceMessage], name = "UserUpdateTraceMessage"),
    new JsonSubTypes.Type(value = classOf[QueryUserInfoMessage], name = "QueryUserInfoMessage"),
    new JsonSubTypes.Type(value = classOf[DelUserMessage], name = "DelUserMessage"),
    new JsonSubTypes.Type(value = classOf[ModifyPasswordMessage], name = "ModifyPasswordMessage"),
    new JsonSubTypes.Type(value = classOf[SearchTraceMessage], name = "SearchTraceMessage"),
    new JsonSubTypes.Type(value = classOf[DelTraceMessage], name = "DelTraceMessage"),
    new JsonSubTypes.Type(value = classOf[DelEarliestTraceMessage], name = "DelEarliestTraceMessage"),
    new JsonSubTypes.Type(value = classOf[AddPlaceMessage], name = "AddPlaceMessage"),
    new JsonSubTypes.Type(value = classOf[ModifyTraceMessage], name = "ModifyTraceMessage"),
    new JsonSubTypes.Type(value = classOf[DelTraceFourteenMessage], name = "DelTraceFourteenMessage"),
    new JsonSubTypes.Type(value = classOf[AddManangerMessage], name = "AddManagerMessage"),
    new JsonSubTypes.Type(value = classOf[ManagerLoginMessage], name = "ManagerLoginMessage"),
    new JsonSubTypes.Type(value = classOf[GiveManagerMessage], name = "GiveManagerMessage"),
    new JsonSubTypes.Type(value = classOf[UserUpdateColorMessage], name = "UserUpdateColorMessage"),
    new JsonSubTypes.Type(value = classOf[QueryRelativeInfoMessage], name = "QueryRelativeInfoMessage"),
    new JsonSubTypes.Type(value = classOf[ShowRelativeMessage], name = "ShowRelativeMessage"),
    new JsonSubTypes.Type(value = classOf[DelRelativeMessage], name = "DelRelativeMessage"),
    new JsonSubTypes.Type(value = classOf[ChangeRateWithNameMessage], name = "ChangeRateWithNameMessage"),
    new JsonSubTypes.Type(value = classOf[ChangeRateWithIDMessage], name = "ChangeRateWithIDMessage"),
    new JsonSubTypes.Type(value = classOf[QueryPlaceIDMessage], name = "QueryPlaceIDMessage"),
    new JsonSubTypes.Type(value = classOf[DelPlaceMessage], name = "DelPlaceMessage"),
    new JsonSubTypes.Type(value = classOf[DelRecordFourteenMessage], name = "DelRecordFourteenMessage"),
    new JsonSubTypes.Type(value = classOf[AddAcidTestMessage], name = "AddAcidTestMessage"),
    new JsonSubTypes.Type(value = classOf[QueryAllAcidMessage], name = "QueryAllAcidMessage"),
    new JsonSubTypes.Type(value = classOf[QueryLastAcidTestMessage], name = "QueryLastAcidTestMessage"),
    new JsonSubTypes.Type(value = classOf[DelFourteenAcidMessage], name = "DelFourteenAcidMessage"),
    new JsonSubTypes.Type(value = classOf[UpdateFirstVaccMessage], name = "UpdateFirstVaccMessage"),
    new JsonSubTypes.Type(value = classOf[UpdateSecondVaccMessage], name = "UpdateSecondVaccMessage"),
    new JsonSubTypes.Type(value = classOf[UpdateThirdVaccMessage], name = "UpdateThirdVaccMessage"),
    new JsonSubTypes.Type(value = classOf[UpdateBoostVaccMessage], name = "UpdateBoostVaccMessage"),
    new JsonSubTypes.Type(value = classOf[GetFirstVaccMessage], name = "GetFirstVaccMessage"),
    new JsonSubTypes.Type(value = classOf[GetSecondVaccMessage], name = "GetSecondVaccMessage"),
    new JsonSubTypes.Type(value = classOf[GetThirdVaccMessage], name = "GetThirdVaccMessage"),
    new JsonSubTypes.Type(value = classOf[GetBoostVaccMessage], name = "GetBoostVaccMessage"),
    new JsonSubTypes.Type(value = classOf[GetPasserMessage], name = "GetPasserMessage"),
    new JsonSubTypes.Type(value = classOf[AutoUpdateUserColorMessage], name = "AutoUpdateUserColorMessage"),
    new JsonSubTypes.Type(value = classOf[AddRequestMessage], name = "AddRequestMessage"),
    new JsonSubTypes.Type(value = classOf[DelRequestMessage], name = "DelRequestMessage"),
    new JsonSubTypes.Type(value = classOf[ShowRequestingMessage], name = "ShowRequestingMessage"),
    new JsonSubTypes.Type(value = classOf[ShowRequestedMessage], name = "ShowRequestedMessage"),
    new JsonSubTypes.Type(value = classOf[AcceptRequestMessage], name = "AcceptRequestMessage"),
    new JsonSubTypes.Type(value = classOf[RejectRequestMessage], name = "RejectRequestMessage"),
  ))
abstract class TSMSPMessage extends JacksonSerializable {
  def handle() : TSMSPReply = reaction(DateTime.now()) match {
    case Success(value) => value
    case Failure(exception) => TSMSPReply(STATUS_ERROR, exception.getMessage)
  }
  def reaction(now : DateTime) : Try[TSMSPReply] = Try(TSMSPReply(STATUS_ERROR, "无法识别的消息"))
}
