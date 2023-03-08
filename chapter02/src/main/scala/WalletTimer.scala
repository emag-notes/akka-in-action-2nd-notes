import akka.actor.typed.Behavior
import akka.actor.typed.scaladsl.Behaviors

import scala.concurrent.duration.DurationInt

object WalletTimer {

  sealed trait Command
  final case class Increase(amount: Int) extends Command
  final case class Deactivate(seconds: Int) extends Command
  private final case object Activate extends Command

  def apply(): Behavior[Command] = activated(0)

  private def activated(total: Int): Behavior[Command] = Behaviors.receive { (context, message) =>
    Behaviors.withTimers { timers =>
      message match {
        case Increase(amount) =>
          val current = total + amount
          context.log.info(s"increasing to $current")
          activated(current)
        case Deactivate(seconds) =>
          timers.startSingleTimer(Activate, seconds.second)
          context.log.info(s"deactivating")
          deactivated(total)
        case Activate =>
          Behaviors.same
      }
    }
  }

  private def deactivated(total: Int): Behavior[Command] = Behaviors.receive { (context, message) =>
    message match {
      case Increase(_) =>
        context.log.info("wallet is deactivated. Can't increase")
        Behaviors.same
      case Deactivate(_) =>
        context.log.info("wallet is deactivated. Can't be deactivated again")
        Behaviors.same
      case Activate =>
        context.log.info(s"activating")
        activated(total)
    }
  }

}
