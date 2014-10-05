package me.guymer.playground.selenium

import org.openqa.selenium.WebDriver
import org.scalatest.selenium.Page
import org.scalatest.selenium.WebBrowser

class HomePage(implicit driver: WebDriver, baseUrl: String) extends Page with WebBrowser {
  override val url = baseUrl

  val usernameId = "userName"
  val passwordId = "password"

  def login(username: String, password: String): LoggedInIndexPage = {
    val usernameField = textField(usernameId)
    usernameField.value = username
    pwdField(passwordId).value = password
    click on usernameField
    submit()

    new LoggedInIndexPage()
  }
}

class LoggedInIndexPage(implicit driver: WebDriver, baseUrl: String) extends Page with WebBrowser {
  override val url = baseUrl

  //cssSelector(".username.menu").findElement
}

class NewUserPage(implicit driver: WebDriver, baseUrl: String) extends Page with WebBrowser {
  override val url = s"$baseUrl/admin/users/_newuser"

  val usernameId = "userName"
  val passwordId = "password"
  val fullNameId = "fullName"
  val mailAddressId = "mailAddress"
  val isAdminName = "isAdmin"
  val urlId = "url"

  def create(username: String, password: String, fullName: String, email: String, isAdmin: Boolean, url: Option[String] = None): UserManagementPage = {
    val usernameField = textField(usernameId)
    usernameField.value = username
    pwdField(passwordId).value = password
    textField(fullNameId).value = fullName
    textField(mailAddressId).value = email
    url.foreach { url => textField(urlId).value = url }

    click on usernameField
    submit()

    new UserManagementPage()
  }
}

class UserManagementPage(implicit driver: WebDriver, baseUrl: String) extends Page with WebBrowser {
  override val url = s"$baseUrl/admin/users"

  //checkbox("includeRemoved")
  def hasUser(username: String, email: String): Boolean = {
    val hasUser = for {
      usernameElement <- xpath(s"//tr/td/div[a/text()='$username']").findElement
      emailElement <- xpath(s"//tr/td/div[contains(.,'$email')]").findElement
    } yield usernameElement.isDisplayed && emailElement.isDisplayed
    hasUser.isDefined
  }
}
