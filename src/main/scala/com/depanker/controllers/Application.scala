//package com.depanker.controllers
//
//import play.api.data.Form
//import play.api.data.Forms._
//import play.api.libs.json.{Json, JsError}
//import play.api.mvc.{Action, Controller}
//
//
//object Applicaion extends  Controller {
//
//  val registration = Form(
//    tuple(
//      "email" -> email,
//      "name" -> optional(text)
//    )
//  )
//
//  def index = Action {implicit request => registration.bindFromRequest().fold(
//    errors => BadRequest(Json.obj("status" -> "OK", "message" -> JsError.toFlatJson(errors))),
//    success => {
//
//      Ok(Json.obj("status" -> "OK"))
//    }
//  )
//
//  }
//}