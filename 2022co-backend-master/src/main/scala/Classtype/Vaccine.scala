package Classtype

import Impl.JacksonSerializable

case class Vaccine(
                  done: Boolean,
                  date: Long,
                  kind: String,
                  ) extends JacksonSerializable
