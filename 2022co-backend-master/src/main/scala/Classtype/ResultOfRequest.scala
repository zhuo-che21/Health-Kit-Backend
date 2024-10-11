package Classtype

import Impl.JacksonSerializable
import com.fasterxml.jackson.core.{JsonGenerator, JsonParser}
import com.fasterxml.jackson.databind.{DeserializationContext, SerializerProvider}
import com.fasterxml.jackson.databind.annotation.{JsonDeserialize, JsonSerialize}
import com.fasterxml.jackson.databind.deser.std.StdDeserializer
import com.fasterxml.jackson.databind.ser.std.StdSerializer

@JsonSerialize(using = classOf[ResultOfRequestTypeSerializer])
@JsonDeserialize(using = classOf[ResultOfRequestTypeDeserializer])
sealed abstract class ResultOfRequest(val v:String) extends JacksonSerializable
case object Accept extends ResultOfRequest("同意")
case object Reject extends ResultOfRequest("拒绝")
case object Unreplied extends ResultOfRequest("未回复")

object ResultOfRequest{
  def objectList: List[ResultOfRequest] =
    List(Accept, Reject, Unreplied)

  def getType(v:String): ResultOfRequest= objectList.filter(_.v==v).head
}

class ResultOfRequestTypeSerializer extends StdSerializer[ResultOfRequest](classOf[ResultOfRequest]) {
  override def serialize(value: ResultOfRequest, gen: JsonGenerator, provider: SerializerProvider): Unit =
    gen.writeString(value.v)
}

class ResultOfRequestTypeDeserializer extends StdDeserializer[ResultOfRequest](classOf[ResultOfRequest]) {
  override def deserialize(p: JsonParser, ctxt: DeserializationContext): ResultOfRequest=
    ResultOfRequest.getType(p.getText)
}




