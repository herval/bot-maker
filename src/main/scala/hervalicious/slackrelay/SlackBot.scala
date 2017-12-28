package hervalicious.slackrelay

import java.net.InetSocketAddress

import com.twitter.finagle.http.path.{Root, _}
import com.twitter.finagle.http.service.RoutingService
import com.twitter.finagle.http.{Request, Response}
import com.twitter.finagle.{Http, ListeningServer, Service}
import hervalicious.slackrelay.api.slashcommands.{SlashCommandHandler, SlashCommandParserFilter, SlashCommandResponder, SlashCommandsService}
import hervalicious.slackrelay.api.{BackgroundJobs, TokenValidationFilter}


case class PartialBot(
                       apiKey: Option[String] = None,
                       verificationToken: Option[String] = None,
                       port: Option[Int] = Some(8080),
                       slashCommands: Seq[SlashCommandHandler] = Seq.empty
                     ) {

  def withKey(slackApiKey: String): PartialBot = this.copy(apiKey = Some(slackApiKey))

  def withToken(verificationToken: String): PartialBot = this.copy(verificationToken = Some(verificationToken))

  def withPort(port: Int): PartialBot = this.copy(port = Some(port))

  def withSlashCommand(handler: SlashCommandHandler): PartialBot = this.copy(
    slashCommands = slashCommands :+ handler
  )

  def build(): SlackBot = {
    this match {
      case PartialBot(Some(k), Some(t), Some(p), slashCmds) => new SlackBot(k, t, p, slashCmds)
      case PartialBot(None, _, _, _) => throw new IllegalStateException("Please configure an API key")
      case PartialBot(_, None, _, _) => throw new IllegalStateException("Please configure a verification token")
      case _ => throw new IllegalStateException("Incomplete configuration")
    }
  }
}

object SlackBot {

  def Builder() = PartialBot()

}

class SlackBot(
                val apiKey: String,
                val verificationToken: String,
                val apiPort: Int,
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
      new InetSocketAddress(apiPort),
      apiRouter
    )
  }

}
