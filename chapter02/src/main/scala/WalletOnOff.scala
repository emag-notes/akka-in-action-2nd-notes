import akka.actor.typed.Behavior
import akka.actor.typed.scaladsl.Behaviors

object WalletOnOff {

  sealed trait Command
  final case class Increase(amount: Int) extends Command
  final case object Activate extends Command
  final case object Deactivate extends Command

  def apply(): Behavior[Command] = activated(0)

  private def activated(total: Int): Behavior[Command] = Behaviors.receive { (context, message) =>
    message match {
      case Increase(amount) =>
        val current = total + amount
        context.log.info(s"increasing to $current")
        activated(current)
      case Activate =>
        Behaviors.same
      case Deactivate =>
        context.log.info(s"deactivating")
        deactivated(total)
    }
  }

  private def deactivated(total: Int): Behavior[Command] = Behaviors.receive { (context, message) =>
    message match {
      case Increase(_) =>
        context.log.info(s"wallet is deactivated. Can't increase")
        Behaviors.same
      case Activate =>
        context.log.info(s"activating")
        activated(total)
      case Deactivate =>
        Behaviors.same
    }
  }
}
