package me.guymer.playground

import java.net.URL

import scala.language.implicitConversions

package object selenium {

  implicit def stringToUrl(string: String) = new URL(string)
}
