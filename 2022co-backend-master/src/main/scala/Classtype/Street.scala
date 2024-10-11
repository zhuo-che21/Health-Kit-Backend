package Classtype

import Impl.JacksonSerializable
import com.fasterxml.jackson.core.{JsonGenerator, JsonParser}
import com.fasterxml.jackson.databind.annotation.{JsonDeserialize, JsonSerialize}
import com.fasterxml.jackson.databind.deser.std.StdDeserializer
import com.fasterxml.jackson.databind.ser.std.StdSerializer
import com.fasterxml.jackson.databind.{DeserializationContext, SerializerProvider}

@JsonSerialize(using = classOf[StreetTypeSerializer])
@JsonDeserialize(using = classOf[StreetTypeDeserializer])
sealed abstract class Street(val v:String) extends JacksonSerializable
case object QinghuaYuan  extends Street("清华园街道")
case object ZhongGuanCun extends Street("中关村街道")
case object YanYuan extends Street("燕园街道")

object Street{
  def objectList: List[Street] =
    List(QinghuaYuan, ZhongGuanCun, YanYuan)

  def getType(v:String): Street= objectList.filter(_.v==v).head
}

class StreetTypeSerializer extends StdSerializer[Street](classOf[Street]) {
  override def serialize(value: Street, gen: JsonGenerator, provider: SerializerProvider): Unit =
    gen.writeString(value.v)
}

class StreetTypeDeserializer extends StdDeserializer[Street](classOf[Street]) {
  override def deserialize(p: JsonParser, ctxt: DeserializationContext): Street=
    Street.getType(p.getText)
}




