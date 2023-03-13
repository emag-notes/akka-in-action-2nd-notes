import akka.actor.typed.scaladsl.Behaviors
import akka.actor.typed.{ ActorRef, Behavior }

import scala.concurrent.duration.DurationInt

object SimplifiedManager {

  sealed trait Command
  final case class CreateChild(name: String) extends Command
  final case class Forward(message: String, sendTo: ActorRef[String]) extends Command
  final case object Log extends Command
  final case object ScheduleLog extends Command

  def apply(): Behavior[Command] = Behaviors.receive { (context, message) =>
    message match {
      case CreateChild(name) =>
        context.spawn(SimplifiedWorker(), name)
        Behaviors.same
      case Forward(message, sendTo) =>
        sendTo ! message
        Behaviors.same
      case Log =>
        context.log.info("it's done")
        Behaviors.same
      case ScheduleLog =>
        context.scheduleOnce(1.seconds, context.self, Log)
        Behaviors.same
    }
  }
}
