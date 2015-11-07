package com.depanker.dao

import com.typesafe.config.ConfigFactory
import jp.sf.amateras.solr.scala.SolrClient

object SolrManager {
  val config  =  ConfigFactory.load()

  val  solrRead = new SolrClient(config.getString("solr.read"))

  val  solrWrite = new SolrClient (config.getString("solr.write"))
}