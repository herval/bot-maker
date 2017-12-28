package hervalicious.slackrelay.api.slashcommands

import com.twitter.util.Future

trait SlashCommandHandler {
  def command: String

  def handler: (SlashCommand, SlashCommandResponder) => Future[Unit]

  // TODO buttonInteraction
}
