package com.depanker.actors.workers

import java.util.Date

import akka.actor.{Actor, ActorLogging}
import akka.actor.Actor.Receive
import com.depanker.base.{SiteElements, MarkupScarper}
import com.depanker.dao.SolrManager
import com.depanker.util.DateUtil
import com.typesafe.config.ConfigFactory

import net.ruippeixotog.scalascraper.browser.Browser
import net.ruippeixotog.scalascraper.dsl.DSL._
import net.ruippeixotog.scalascraper.dsl.DSL.Extract._
import net.ruippeixotog.scalascraper.dsl.DSL.Parse._
import net.ruippeixotog.scalascraper.scraper.ContentExtractors._
import org.jsoup.{Jsoup, Connection}


//import org.jsoup.nodes.Element
/**
 * Created by depankersharma on 04/10/15.
 */


class SchoolData extends Actor with ActorLogging {
  import SchoolData._


  val browser = new Browser
  val primaryHost = config.getString("schools.baseurl")
  val firstPage = config.getString("schools.firstPage") //"/delhi-school-list" //"http://localhost/~depankersharma/delhi-school-list.html"
  val subUrlSelectors = ".pager"
  val schoolsSelector = ".school-list-main"
  val schoolNameSelector = "span[itemprop=name]"
  val schoolUrlSelector = "div.school-list-schl-website"
  val formsubmissionDateSelector = ".school-list-cy-app-issue"
  val dataManager = context.actorSelection("./dataManager");



  override def postStop() {
    log.info("Terminating...")
  }


  /**
   *
   */
  def  parseData: Unit = {

    val url = getAbsoluteUrl(firstPage)
    val conn: Connection = Jsoup.connect(url)
    conn.
      userAgent("Mozilla/5.0 (Macintosh; Intel Mac OS X 10_10_5) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/46.0.2490.71 Safari/537.36").
      header("Accept", "text/html,application/xhtml+xml,application/xml").
      header("Accept-Charset", "utf-8").
      timeout(Int.MaxValue).
      maxBodySize(0)

    browser.requestSettings(conn)

    self ! url

    val content = browser.get(url)


    val   pages = content  >> elementList("li.pager-item") >> attr("href")("a")


    log.debug("Extracted", pages)

    for(page <- pages) {
      log.debug(s"Sending url $page")
      self ! page
    }
  }

  override def receive: Receive = {
    case  Init => {

      log.info(primaryHost)
      log.debug("DEBUG Got init -")

      Thread.sleep(10000)

      log.info("INFO Got init")
      parseData
//      val firstDoc = browser.get(url)
//      val  otherPages = firstDoc.getElementsByClass(subUrlSelectors)

    }

    case someUrl: String => {
      val url = getAbsoluteUrl(someUrl)
      log.info(s"Parsing URL $url")

      val conn: Connection = Jsoup.connect(url)
      conn.
        userAgent("Mozilla/5.0 (Macintosh; Intel Mac OS X 10_10_5) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/46.0.2490.71 Safari/537.36").
        header("Accept", "text/html,application/xhtml+xml,application/xml").
        header("Accept-Charset", "utf-8").
        timeout(Int.MaxValue).
        maxBodySize(0)

      browser.requestSettings(conn)

      val document = browser.get(url)
      val schools = document >> elementList(schoolsSelector)

      for(school <- schools) {
        val name = school >> text(schoolNameSelector)
        val url  = school >> text(schoolUrlSelector)
        val date  = school >> text(formsubmissionDateSelector)
        log.debug(s"Name: {$name}")
        log.debug(s"Date: {$date}")
        log.debug(s"url: {$url}" )

        val docToSave = Map("schoolName" -> name, "schoolUrl" -> url, "expectedDate" -> parseDate(date),
          "rawDate" -> date, "latestUpdate" -> new Date())

        val result = SolrManager.solrRead.query("schoolUrl: %schoolUrl%")
          .getResultAsMap(Map{"schoolUrl" -> url})

        val  savedDoc = result.documents.head

        if(savedDoc != null && savedDoc.contains("expectedDate")) {

          val expectedDate: Date =  savedDoc("expectedDate").asInstanceOf[Date]

          if(!expectedDate.equals(docToSave("expectedDate"))) {
            dataManager ! Save(docToSave)
          }
        }

       }

      log.info("Process complete.")
    }

    case Shutdown => {
      context become shuttingDown
    }
  }

  def shuttingDown: Receive = {
    case Terminate =>
      context stop self

    case _ => sender() ! "service unavailable, shutting down"
  }

  def getAbsoluteUrl(url: String): String = {
    if(url.contains(primaryHost)) {
      url
    } else {
      primaryHost+url
    }

  }

  def parseDate(date: String) : Date = {

    val result = DateUtil.parserDate(date);

    result getOrElse null
  }
}


class  School (name: String, openingDate: String, url: String, imageUrl: String = "")
object SchoolData {
  private val config =  ConfigFactory.load()

  case object Init
  case object  Shutdown
  case object  Terminate
  case class Urls(val urls: List[String])
}