package errorkernel

import akka.actor.typed.scaladsl.Behaviors
import akka.actor.typed.{ActorRef, Behavior}

object Worker {

  sealed trait Command
  final case class Parse(replyTo: ActorRef[Worker.Response], text: String) extends Command

  sealed trait Response
  final case class Done(text: String) extends Response

  def apply(): Behavior[Command] = Behaviors.receive { (context, message) =>
    message match {
      case Parse(replyTo, text) =>
        val parsed = naiveParsing(text)
        context.log.info(s"'${context.self} DONE! Parsed result: $parsed")
        replyTo ! Worker.Done(parsed)
        Behaviors.stopped
    }
  }

  private def naiveParsing(text: String): String = text.replaceAll("-", "")
}
