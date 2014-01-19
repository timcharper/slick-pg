package controllers

import play.api._
import play.api.data._
import play.api.data.Forms._
import play.api.mvc._
import play.api.db.slick._
import models._
import play.api.Play.current
import my.utils._

//stable imports to use play.api.Play.current outside of objects:
import models.current.dao._
import models.current.dao.driver.simple._

object Application extends Controller{
  def index = DBAction { implicit rs =>
    Ok(views.html.index(Query(OsmWays).list))
  }

  val catForm = Form(
    mapping(
      "id" -> number,
      "version" -> number,
      "userId" -> number,
      "location" -> point,
      "tstamp" -> datetime,
      "changeSetId" -> number,
      "tags" -> strMap,
      "nodes" -> list(number)
    )(OsmWay.apply)(OsmWay.unapply)
  )
  
  def insert = DBAction{ implicit rs =>
    val cat = catForm.bindFromRequest.get
    OsmWays.insert(cat)
    Redirect(routes.Application.index)
  }
  
}
