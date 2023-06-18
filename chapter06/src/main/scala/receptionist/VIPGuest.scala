package receptionist

import akka.actor.typed.Behavior
import akka.actor.typed.receptionist.Receptionist
import akka.actor.typed.scaladsl.Behaviors

object VIPGuest {

  sealed trait Command
  final case object EnterHotel extends Command
  final case object LeaveHotel extends Command

  def apply(): Behavior[Command] = Behaviors.receive[Command] { (context, message) =>
    message match {
      case EnterHotel =>
        context.system.receptionist ! Receptionist.Register(HotelConcierge.goldenKey, context.self)
        Behaviors.same
      case LeaveHotel =>
        context.system.receptionist ! Receptionist.Deregister(HotelConcierge.goldenKey, context.self)
        Behaviors.same
    }
  }
}
