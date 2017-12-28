package hervalicious.slackrelay.api.slashcommands

import com.twitter.finagle.Service
import com.twitter.finagle.http.{Response, Status, Version}
import com.twitter.util.Future
import hervalicious.slackrelay.api.BackgroundJobs
import hervalicious.slackrelay.api.slashcommands.ResponseContext.ResponseContext

object ResponseContext extends Enumeration {
  type ResponseContext = Value

  val RequesterOnly = Value // respond to the user only
  val InChannel = Value // respond to the channel (visible to other users)
}

case class SlashCommandResponse(
                                 text: String,
                                 respondTo: ResponseContext
                               )

class SlashCommandsService(handlers: Seq[SlashCommandHandler], executor: BackgroundJobs) extends Service[SlashCommand, Response] {
  override def apply(req: SlashCommand): Future[Response] = {

    // spin up a bunch of futures to respond to the command
    handlers.filter { h => h.command == req.command }.map { cmd =>
      executor.submit {
        cmd.handler(req, SlashCommandResponder(req))
      }
    } match {
      case Seq.empty =>
        // no handler matched
        Future(
          Response(Version.Http11, Status.BadRequest)
        )

      case _ =>
        // respond with 200 to tell Slack everything is 👍
        Future(
          Response(Version.Http11, Status.Ok)
        )
    }
  }
}
