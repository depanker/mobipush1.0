package com.depanker.base

import java.util.Date

/**
 * Created by depankersharma on 04/10/15.
 */
trait MarkupScarper
case class RssReader(title: String, link: String, description: String, category: String, date: Date) extends MarkupScarper
case class Html(title: String, link: String, description: String, category: String, date: Date) extends MarkupScarper
