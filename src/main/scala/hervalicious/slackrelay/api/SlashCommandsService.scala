package hervalicious.slackrelay.api

import com.twitter.finagle.Service
import com.twitter.finagle.http.{Request, Response, Status, Version}
import com.twitter.util.Future

import scala.util.parsing.json.JSONObject

class SlashCommandsService extends Service[Request, Response] {
  override def apply(req: Request): Future[Response] = {
    val rep = Response(Version.Http11, Status.Ok)
    // scala.util.parsing.json.JSONObject
    val o = JSONObject(Map("id" -> id, "name" -> "John Smith"))
    rep.setContentTypeJson()
    rep.setContentString(o.toString)

    Future(rep)
  }
}
