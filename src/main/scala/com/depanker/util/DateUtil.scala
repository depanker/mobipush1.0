package com.depanker.util

import java.util.Date
import javax.script.ScriptEngine

import akka.event.slf4j.Logger
import com.caucho.quercus.script.QuercusScriptEngineFactory

/**
 * Created by depankersharma on 07/11/15.
 */
object DateUtil {

  var logger = Logger(Debug.getType(DateUtil))

  val format = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss")

  def  parserDate(date: String): Option[Date] = {

    var result = None: Option[Date]

    try {
      val factory: QuercusScriptEngineFactory = new QuercusScriptEngineFactory();
      val engine: ScriptEngine = factory.getScriptEngine()
      val  parseableDate = engine.eval(s"<?php return date('Y-m-d 00:00:00', strtotime('Dec 1st week')); ?>")

      logger.debug(parseableDate.toString)

      result = Some(format.parse(parseableDate.toString))
    } catch {
      case e : Exception => {
        logger.error(e.getMessage, e)
      }
    }

    result
  }


}
