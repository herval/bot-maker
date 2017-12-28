package hervalicious.slackrelay

import java.net.InetSocketAddress

import com.twitter.finagle.http.path.{Root, _}
import com.twitter.finagle.http.service.RoutingService
import com.twitter.finagle.http.{Request, Response}
import com.twitter.finagle.{Http, ListeningServer, Service}
import hervalicious.slackrelay.api.slashcommands.{SlashCommandHandler, SlashCommandParserFilter, SlashCommandsService}
import hervalicious.slackrelay.api.{BackgroundJobs, TokenValidationFilter}


case class SlackBot(
                     val clientId: String,
                     val clientSecret: String,
                     val verificationToken: String,
                     val port: Int,
                     val slashCommands: Seq[SlashCommandHandler]
                   ) {

  private val jobsExecutor = new BackgroundJobs()

  private val slashCommandsHandler: Service[Request, Response] = {
    new TokenValidationFilter(verificationToken) andThen
      new SlashCommandParserFilter() andThen
      new SlashCommandsService(slashCommands, jobsExecutor)
  }

  private val apiRouter = RoutingService.byPathObject[Request] {
    //    case Root / "interactive-commands" / "callback" => callbacks(id)
    case Root / "slash-commands" / "callback" => slashCommandsHandler
    //    case _ => blackHole

  }

  def start(): ListeningServer = {
    Http.server.serve(
      new InetSocketAddress(port),
      apiRouter
    )
  }

}
