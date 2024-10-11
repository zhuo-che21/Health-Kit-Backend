package Classtype

import Impl.JacksonSerializable
import com.fasterxml.jackson.core.{JsonGenerator, JsonParser}
import com.fasterxml.jackson.databind.{DeserializationContext, SerializerProvider}
import com.fasterxml.jackson.databind.annotation.{JsonDeserialize, JsonSerialize}
import com.fasterxml.jackson.databind.deser.std.StdDeserializer
import com.fasterxml.jackson.databind.ser.std.StdSerializer

@JsonSerialize(using = classOf[DistrictTypeSerializer])
@JsonDeserialize(using = classOf[DistrictTypeDeserializer])
sealed abstract class District(val v:String) extends JacksonSerializable
case object HaiDian extends District("海淀区")
case object ChaoYang extends District("朝阳区")
case object ChangPing extends District("昌平区")

object District{
  def objectList: List[District] =
    List(HaiDian, ChangPing, ChaoYang)

  def getType(v:String): District= objectList.filter(_.v==v).head
}

class DistrictTypeSerializer extends StdSerializer[District](classOf[District]) {
  override def serialize(value: District, gen: JsonGenerator, provider: SerializerProvider): Unit =
    gen.writeString(value.v)
}

class DistrictTypeDeserializer extends StdDeserializer[District](classOf[District]) {
  override def deserialize(p: JsonParser, ctxt: DeserializationContext): District=
    District.getType(p.getText)
}



//object District extends Enumeration {
//  type District =Value
//  val 海淀区 = Value(1)
//  val 朝阳区 = Value(2)
//}
