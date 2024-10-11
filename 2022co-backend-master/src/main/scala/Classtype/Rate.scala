package Classtype

import Impl.JacksonSerializable
import com.fasterxml.jackson.core.{JsonGenerator, JsonParser}
import com.fasterxml.jackson.databind.{DeserializationContext, SerializerProvider}
import com.fasterxml.jackson.databind.annotation.{JsonDeserialize, JsonSerialize}
import com.fasterxml.jackson.databind.deser.std.StdDeserializer
import com.fasterxml.jackson.databind.ser.std.StdSerializer

@JsonSerialize(using = classOf[RateTypeSerializer])
@JsonDeserialize(using = classOf[RateTypeDeserializer])
sealed abstract class Rate(val v:String) extends JacksonSerializable
case object High extends Rate("高风险")
case object Middle extends Rate("中风险")
case object Low extends Rate("低风险")
case object Normal extends Rate("常态化风险地区")//常态化防控区域

object Rate{
  def objectList: List[Rate] =
    List(High, Middle, Low, Normal)

  def getType(v:String): Rate= objectList.filter(_.v==v).head
}

class RateTypeSerializer extends StdSerializer[Rate](classOf[Rate]) {
  override def serialize(value: Rate, gen: JsonGenerator, provider: SerializerProvider): Unit =
    gen.writeString(value.v)
}

class RateTypeDeserializer extends StdDeserializer[Rate](classOf[Rate]) {
  override def deserialize(p: JsonParser, ctxt: DeserializationContext): Rate=
    Rate.getType(p.getText)
}


