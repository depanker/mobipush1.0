package com.depanker.actors.manager

import akka.actor._
import akka.routing.RoundRobinPool
import com.depanker.actors.workers.{DataManager, SchoolData}


object Manager {
  case object Shutdown
}

class Manager extends Actor with ActorLogging {
  import Manager._



//  val system = ActorSystem("MyActorSystem")
//  val worker  = system.actorOf(Props[SchoolData], "manager")

  val schoolData = context.watch(context.actorOf(Props[SchoolData]withRouter(RoundRobinPool(5)), "schoolData"))
  val dataManger = context.watch(context.actorOf(Props[DataManager]withRouter(RoundRobinPool(5)), "dataManager"))

  def receive = {

    case Shutdown => {

      context stop  schoolData
      context stop  dataManger

      log.info("sending stop")
      context become shuttingDown

    }

    case SchoolData.Init => schoolData ! SchoolData.Init
  }

  def shuttingDown: Receive = {

    case Terminated(worker) =>
      context stop self

    case _ => {
      log.info("service unavailable, shutting down")
      sender() ! "service unavailable, shutting down"
    }
  }
}