package hervalicious.slackrelay.api

import com.twitter.finagle.{Filter, Service}
import com.twitter.finagle.http.{Request, Response, Status}
import com.twitter.util.Future

// Validate that a token is present on the request
class TokenValidationFilter(
                             slackToken: String
                           ) extends Filter[Request, Response, Request, Response] {

  override def apply(request: Request, service: Service[Request, Response]): Future[Response] = {
    request.params.get("token") match {
      case None =>
        Future.value(
          Response(Status.BadRequest)
        )
      case Some(token) if token != slackToken =>
        Future.value(
          Response(Status.Unauthorized)
        )
      case _ => service(request)
    }
  }
}
