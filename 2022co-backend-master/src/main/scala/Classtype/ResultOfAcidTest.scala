package Classtype

import Impl.JacksonSerializable
import com.fasterxml.jackson.core.{JsonGenerator, JsonParser}
import com.fasterxml.jackson.databind.{DeserializationContext, SerializerProvider}
import com.fasterxml.jackson.databind.annotation.{JsonDeserialize, JsonSerialize}
import com.fasterxml.jackson.databind.deser.std.StdDeserializer
import com.fasterxml.jackson.databind.ser.std.StdSerializer

@JsonSerialize(using = classOf[ResultOfAcidTypeSerializer])
@JsonDeserialize(using = classOf[ResultOfAcidTypeDeserializer])
sealed abstract class ResultOfAcid(val v:String) extends JacksonSerializable
case object Negative extends ResultOfAcid("阴性")
case object Positive extends ResultOfAcid("阳性")
case object Yet extends ResultOfAcid("检测中")

object ResultOfAcid{
  def objectList: List[ResultOfAcid] =
    List(Negative, Positive, Yet)

  def getType(v:String): ResultOfAcid= objectList.filter(_.v==v).head
}

class ResultOfAcidTypeSerializer extends StdSerializer[ResultOfAcid](classOf[ResultOfAcid]) {
  override def serialize(value: ResultOfAcid, gen: JsonGenerator, provider: SerializerProvider): Unit =
    gen.writeString(value.v)
}

class ResultOfAcidTypeDeserializer extends StdDeserializer[ResultOfAcid](classOf[ResultOfAcid]) {
  override def deserialize(p: JsonParser, ctxt: DeserializationContext): ResultOfAcid=
    ResultOfAcid.getType(p.getText)
}


