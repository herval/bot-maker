package hervalicious.slackrelay.api.slashcommands

import com.twitter.finagle.http.{Request, Response, Status}
import com.twitter.finagle.{Filter, Service}
import com.twitter.util.Future

case class SlashCommand(
                         teamId: String,
                         teamDomain: String,
                         enterpriseId: String,
                         enterpriseName: String,
                         channelId: String,
                         channelName: String,
                         userId: String,
                         command: String,
                         text: String,
                         responseUrl: String,
                         triggerId: String
                       )

class SlashCommandParserFilter extends Filter[Request, Response, SlashCommand, Response] {

  /*
   * Slack POSTs slash-commands with the following attributes:
   *
   *  token=gIkuvaNzQIHg97ATvDxqgjtO
   *  team_id=T0001
   *  team_domain=example
   *  enterprise_id=E0001
   *  enterprise_name=Globular%20Construct%20Inc
   *  channel_id=C2147483705
   *  channel_name=test
   *  user_id=U2147483697
   *  command=/weather
   *  text=94070
   *  response_url=https://hooks.slack.com/commands/1234/5678
   *  trigger_id=13345224609.738474920.8088930838d88f008e0
   *
   */
  override def apply(request: Request, service: Service[SlashCommand, Response]): Future[Response] = {
    val params = request.params
    val cmd = for {
      teamId <- params.get("team_id")
      teamDomain <- params.get("team_domain")
      enterpriseId <- params.get("enterprise_id")
      enterpriseName <- params.get("enterprise_name")
      channelId <- params.get("channel_id")
      channelName <- params.get("channel_name")
      userId <- params.get("user_id")
      command <- params.get("command")
      text <- params.get("text")
      responseUrl <- params.get("response_url")
      triggerId <- params.get("trigger_id")
    } yield {
      SlashCommand(
        teamId,
        teamDomain,
        enterpriseId,
        enterpriseName,
        channelId,
        channelName,
        userId,
        command,
        text,
        responseUrl,
        triggerId
      )
    }

    cmd match {
      case Some(command) => service(command)
      case None => Future.value(
        Response(Status.BadRequest)
      )
    }
  }

}
