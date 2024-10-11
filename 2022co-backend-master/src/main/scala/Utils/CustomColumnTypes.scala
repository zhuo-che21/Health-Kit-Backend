package Utils

import Impl.JacksonSerializable
import slick.ast.BaseTypedType
import slick.jdbc.PostgresProfile.MappedColumnType
import slick.jdbc.JdbcType
import slick.jdbc.PostgresProfile.api._

import scala.reflect.ClassTag

object CustomColumnTypes {

  implicit def jacksonSerializableType[T <: JacksonSerializable](implicit c: ClassTag[T]): JdbcType[T] with BaseTypedType[T] = {
    MappedColumnType.base[T, String](
      IOUtils.serialize(_).get,
      IOUtils.deserialize[T](_).get
    )
  }
}
//
//这是我们整个项目用的隐式转换
///** 用于数据库的自动类型转换 */
//object CustomColumnTypes {
//  implicit val jodaDateTimeType: JdbcType[DateTime] with BaseTypedType[DateTime] =
//    MappedColumnType.base[DateTime, Timestamp](
//      dt => new Timestamp(dt.getMillis),
//      ts => new DateTime(ts.getTime)
//    )
//
//  implicit def listType[T](implicit tag: ClassTag[T]): JdbcType[List[T]] with BaseTypedType[List[T]] =
//    MappedColumnType.base[List[T], String](
//      a => {
//        IOUtils.serializeList(a).get
//      },
//      a => {
//        IOUtils.deserializeList[T](a).get
//      }
//    )
//
//  implicit def jacksonSerializableType[T <: JacksonSerializable](implicit c: ClassTag[T]): JdbcType[T] with BaseTypedType[T] = {
//    MappedColumnType.base[T, String](
//      IOUtils.serialize(_).get,
//      IOUtils.deserialize[T](_).get
//    )
//  }
//
//  implicit def longType[T <: IDClass](implicit c: ClassTag[T]): JdbcType[T] with BaseTypedType[T] =
//    MappedColumnType.base[T, Long](
//      _.v,
//      a => {
//        c.runtimeClass.getConstructors.head.newInstance(a.asInstanceOf[Object]).asInstanceOf[T]
//      }
//    )
//
