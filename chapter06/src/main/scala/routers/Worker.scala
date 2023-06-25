package routers

import akka.actor.typed.scaladsl.Behaviors
import akka.actor.typed.{ActorRef, Behavior}

object Worker {

  def apply(monitor: ActorRef[String]): Behavior[String] = Behaviors.receiveMessage {
    message =>
      monitor ! message
      Behaviors.same
  }
}
