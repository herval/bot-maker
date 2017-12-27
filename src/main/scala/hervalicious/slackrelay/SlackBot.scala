package hervalicious.slackrelay

import java.net.InetSocketAddress

import com.twitter.finagle.{Http, ListeningServer, Service, Stack}
import com.twitter.finagle.builder.ServerBuilder
import com.twitter.finagle.http.path.{Root, _}
import com.twitter.finagle.http.service.RoutingService
import com.twitter.finagle.http.{Request, Response}
import com.twitter.util.Await
import hervalicious.slackrelay.api.SlashCommandsService

trait SlashCommand


object SlackBot {

  def Builder() = PartialBot()

  case class PartialBot(
                         key: Option[String] = None,
                         port: Option[Int] = Some(8080),
                         slashCommands: Seq[SlashCommand] = Seq.empty
                       ) {

    def withKey(slackApiKey: String): PartialBot = this.copy(key = Some(slackApiKey))

    def withPort(port: Int): PartialBot = this.copy(port = Some(port))

    def withSlashCommand(command: SlashCommand) = this.copy(
      slashCommands = slashCommands :+ command
    )

    def newBot(): SlackBot = {
      this match {
        case PartialBot(Some(key), Some(port), slashCommands) => new SlackBot(key, port)
        case PartialBot(None, _, _) => throw new IllegalStateException("Please configure an API key")
        case _ => throw new IllegalStateException("Incomplete configuration")
      }
    }
  }

}

private class SlackBot(
                        val slackKey: String,
                        val apiPort: Int
                      ) {

  val slashCommandsHandler: Service[Request, Response] = new SlashCommandsService()

  val apiRouter = RoutingService.byPathObject[Request] {
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
