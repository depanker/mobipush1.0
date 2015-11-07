package com.depanker.actors.workers

import akka.actor.{ActorLogging, Actor}
import akka.actor.Actor.Receive
import com.depanker.dao.SolrManager


trait DataSourceOperation
  /**
   * This function will
   * return true is data got successfully
   * otherwise false
   *
   * @param data
   *
   * @return
   */
  case class Save(val data: Map[String, Any]) extends DataSourceOperation

  /**
   * This will return a
   * list  of boolean values,
   * value's which get stored have
   * boolean true to there
   * corresponding index
   *
   * @param data
   * @return
   */
  case class  SaveMany(val data: List[Map[String, Any]]) extends DataSourceOperation

/**
 * Created by depankersharma on 17/10/15.
 */
class DataManager extends Actor with ActorLogging {



  def save(data: Map[String, Any]): Unit = {

    if(data.nonEmpty) SolrManager.solrWrite.add(data).commit

  }

  def save(data: List[Map[String, Any]]): Unit = {

    if(data.nonEmpty) {

      data map {
        doc => SolrManager.solrWrite.add(doc)
      }

      SolrManager.solrWrite.commit

    }
  }

  override def receive: Receive = {

    case message: Save => {
      save(message.data)
    }

    case message: SaveMany => {
      save(message.data)
    }
  }
}
