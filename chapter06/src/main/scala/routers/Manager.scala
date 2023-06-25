package routers

import akka.actor.typed.{ActorRef, Behavior}
import akka.actor.typed.scaladsl.{Behaviors, Routers}

object Manager {

  def apply(behavior: Behavior[String]) = Behaviors.setup[Unit] { context =>
    val routingBehavior: Behavior[String] = Routers.pool(poolSize = 4)(behavior)
    val router: ActorRef[String] = context.spawn(routingBehavior, "test-pool")
    (0 to 10).foreach { n =>
      router ! "hi"
    }
    Behaviors.empty
  }
}
