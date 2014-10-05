package me.guymer.playground.selenium

import scala.util.Random

import org.scalatest.FlatSpec
import org.scalatest.Matchers

class UserSpec extends FlatSpec with Selenium with Matchers {

  val adminUsername = "root"
  val adminPassword = "root"

  "The new user page" should "allow new users to be created" in {
    val homePage = new HomePage
    go to homePage
    homePage.login(adminUsername, adminPassword)

    eventually {
      val usernameMenu = cssSelector(".username.menu").findElement
      usernameMenu should be('defined)
    }

    val username = randomString(10)
    val fullName = randomString(10)
    val email = s"$username@example.com"

    val newUserPage = new NewUserPage
    go to newUserPage
    val users = newUserPage.create(
      username = username,
      password = "password",
      fullName = fullName,
      email = email,
      isAdmin = false
    )

    eventually {
      users.hasUser(username, email) should be(true)
    }
  }

  private def randomString(numChars: Int): String = Random.alphanumeric.take(10).mkString
}
