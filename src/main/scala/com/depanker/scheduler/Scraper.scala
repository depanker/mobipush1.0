package com.depanker.scheduler

import akka.actor.{PoisonPill, Props, ActorSystem}
import akka.event.slf4j.Logger
import com.depanker.actors.manager.Manager
import com.depanker.actors.workers.SchoolData
import com.depanker.util.Debug
import org.slf4j.LoggerFactory
import scala.concurrent.{Future, Await}
import scala.concurrent.duration._
import scala.concurrent.ExecutionContext.Implicits.global
import akka.pattern.gracefulStop


import javax.script.ScriptEngine;
import com.caucho.quercus.script.QuercusScriptEngineFactory;

/**
 * Created by depankersharma on 04/10/15.
 */
object Scraper extends App {

  var logger = Logger(Debug.getType(Scraper))

  sys addShutdownHook(shutdown)





  val system = ActorSystem("MyActorSystem")
  val managerActor = system.actorOf(Props[Manager], "manager")


  val cancellable =
    system.scheduler.schedule(0 milliseconds,
      60 milliseconds,
      managerActor,
      SchoolData.Init)


  private def shutdown {
    logger.info("Shutdown hook caught.")
    Thread.sleep(10000)
    cancellable.cancel()


    while (!cancellable.isCancelled) {
      logger.debug("Trying to cancle...")
    }

    try {
      val stopped: Future[Boolean] = gracefulStop(managerActor, 60 seconds, Manager.Shutdown)
      Await.result(stopped, 60 seconds)
      logger.debug("Shutdown successful.")
      // the actor has been stopped
    } catch {
      // the actor wasn't stopped within 5 seconds
      case e: akka.pattern.AskTimeoutException =>
        logger.error(e.getMessage, e)
    }

    logger.info("Done shutting down.")
  }

  // This example app will ping pong 3 times and thereafter terminate the ActorSystem -
  // see counter logic in PingActor
//  system.awaitTermination()




}
