package me.guymer.playground.selenium

import java.util.concurrent.TimeUnit

import scala.concurrent.duration.Duration
import scala.concurrent.duration.DurationInt
import scala.language.implicitConversions
import scala.language.postfixOps

import org.openqa.selenium.{ Dimension => SeleniumDimension }
import org.openqa.selenium.WebDriver
import org.openqa.selenium.phantomjs.PhantomJSDriver
import org.openqa.selenium.remote.DesiredCapabilities
import org.openqa.selenium.remote.RemoteWebDriver
import org.scalatest.Args
import org.scalatest.BeforeAndAfterAll
import org.scalatest.BeforeAndAfterEach
import org.scalatest.Reporter
import org.scalatest.Status
import org.scalatest.Suite
import org.scalatest.SuiteMixin
import org.scalatest.concurrent.Eventually
import org.scalatest.events.Event
import org.scalatest.events.TestFailed
import org.scalatest.selenium.Driver
import org.scalatest.selenium.WebBrowser
import org.scalatest.time.Nanoseconds
import org.scalatest.time.Span

// https://github.com/matthewfarwell/scaladays-2014-selenium/blob/master/src/test/scala/uk/co/farwell/scaladays/selenium/Common.scala
trait Selenium extends SuiteMixin with BeforeAndAfterAll with BeforeAndAfterEach with WebBrowser with Driver with Eventually {
  self: Suite =>

  implicit val webDriver: WebDriver = {
    val capabilities = new DesiredCapabilities()
    capabilities.setJavascriptEnabled(true)

    //val webDriver = new RemoteWebDriver(webDriverUrl, capabilities)
    val webDriver = new PhantomJSDriver

    val size = webDriverSize
    webDriver.manage().window().setSize(new SeleniumDimension(size._1, size._2))

    val timeouts = webDriver.manage().timeouts()
    timeouts.pageLoadTimeout(pageLoadTimeout, TimeUnit.NANOSECONDS)
    timeouts.setScriptTimeout(scriptTimeout, TimeUnit.NANOSECONDS)
    timeouts.implicitlyWait(0, TimeUnit.SECONDS) // use eventually when waiting is required

    webDriver
  }

  implicit val baseUrl = "http://localhost:8789" // local web driver
  //implicit val baseUrl = "http://gitbucket:8789" // remote web driver in docker container

  implicit def durationToScaledSpan(duration: Duration): Span = scaled(Span(duration.toNanos, Nanoseconds))
  implicit def durationToScaledSpanNanos(duration: Duration): Long = durationToScaledSpan(duration).totalNanos

  implicit val config: PatienceConfig = PatienceConfig(timeout = explicitTimeout, interval = explicitInterval)

  setCaptureDir(captureDir)

  def webDriverUrl: String = "http://127.0.0.1:4444"
  def webDriverSize: (Int, Int) = (1280, 720)
  def captureDir: String = "/tmp/capture"
  def explicitTimeout: Duration = 5 seconds
  def explicitInterval: Duration = 500 milliseconds
  def pageLoadTimeout: Duration = 10 seconds
  def scriptTimeout: Duration = 5 seconds

  override abstract def run(testName: Option[String], args: Args): Status = {
    val seleniumReporter = new SeleniumReporter(args.reporter, this, captureDir)
    super.run(testName, args.copy(reporter = seleniumReporter))
  }

  override def beforeEach {
    webDriver.manage().deleteAllCookies()
    super.beforeEach
  }

  override def afterAll {
    try super.afterAll finally quit()
  }
}

class SeleniumReporter(val aggregateReporter: Reporter, web: WebBrowser, captureDir: String)(implicit val webDriver: WebDriver) extends Reporter {

  override def apply(event: Event): Unit = {
    aggregateReporter.apply(event)
    event match {
      case e: TestFailed => {
        val filePrefix = s"${e.suiteName}.${e.testName}.${System.currentTimeMillis}"
        web.captureTo(s"$filePrefix.png")

        val htmlFile = s"$captureDir/$filePrefix.html"
        writeToFile(htmlFile, web.pageSource)
      }
      case _ => // ignore
    }
  }

  private def writeToFile(f: String, s: String) {
    val p = new java.io.PrintWriter(f)
    try p.write(s) finally p.close()
  }
}
