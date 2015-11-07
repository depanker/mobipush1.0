package com.depanker

import akka.actor.{PoisonPill, Props, ActorSystem}
import com.depanker.actors.workers.SchoolData
import com.typesafe.config.{Config, ConfigFactory}
import scala.concurrent.duration._
import scala.concurrent.ExecutionContext.Implicits.global



//object Main extends App {
////  val conf: Config = ConfigFactory.load("reference.conf")
//  val system = ActorSystem("MyActorSystem")
//  val pingActor = system.actorOf(Props[SchoolData], "SchoolData")
//  pingActor ! SchoolData.Init
//
//  // This example app will ping pong 3 times and thereafter terminate the ActorSystem -
//  // see counter logic in PingActor
//  system.awaitTermination()
//
//  def terminate(): Unit = {
//    pingActor ! PoisonPill
//  }
//}