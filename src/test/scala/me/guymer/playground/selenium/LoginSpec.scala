package me.guymer.playground.selenium

import org.scalatest.FlatSpec
import org.scalatest.Matchers

class LoginSpec extends FlatSpec with Selenium with Matchers {

  val username = "root"
  val password = "root"

  "The home page" should "allows users to log in" in {
    val homePage = new HomePage
    go to homePage

    homePage.login(username, password)

    eventually {
      val usernameMenu = cssSelector(".username.menu").findElement
      usernameMenu should be('defined)
    }
  }

  "The home page" should "not allow invalid credentials" in {
    val homePage = new HomePage
    go to homePage

    homePage.login(username, s"${password}a")

    eventually {
      val signInForm = className("signin-form").findElement
      signInForm should be('defined)
    }
  }
}
