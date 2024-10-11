package Classtype

import Impl.JacksonSerializable

/**
 *
 * @param district 区
 * @param street  街道
 * @param num 门牌号
 */
case class Place (district: District,
                        street: Street,
                        num: String) extends JacksonSerializable