package Classtype

import Impl.JacksonSerializable
import com.fasterxml.jackson.core.{JsonGenerator, JsonParser}
import com.fasterxml.jackson.databind.{DeserializationContext, SerializerProvider}
import com.fasterxml.jackson.databind.annotation.{JsonDeserialize, JsonSerialize}
import com.fasterxml.jackson.databind.deser.std.StdDeserializer
import com.fasterxml.jackson.databind.ser.std.StdSerializer

@JsonSerialize(using = classOf[ColorTypeSerializer])
@JsonDeserialize(using = classOf[ColorTypeDeserializer])
sealed abstract class Color(val v:String) extends JacksonSerializable
case object Green extends Color("green")
case object Yellow extends Color("yellow")
case object Red extends Color("red")

object Color{
  def objectList: List[Color] =
    List(Green, Yellow, Red)

  def getType(v:String): Color= objectList.filter(_.v==v).head
}

class ColorTypeSerializer extends StdSerializer[Color](classOf[Color]) {
  override def serialize(value: Color, gen: JsonGenerator, provider: SerializerProvider): Unit =
    gen.writeString(value.v)
}

class ColorTypeDeserializer extends StdDeserializer[Color](classOf[Color]) {
  override def deserialize(p: JsonParser, ctxt: DeserializationContext): Color=
    Color.getType(p.getText)
}


