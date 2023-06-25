package routers

import akka.actor.typed.scaladsl.{ Behaviors, Routers }
import akka.actor.typed.{ ActorRef, Behavior }

object BroadcastingManager {

  def apply(behavior: Behavior[String]) = Behaviors.setup[Unit] { context =>
    val routingBehavior: Behavior[String] =
      Routers.pool(poolSize = 4)(behavior).withBroadcastPredicate(msg => msg.length > 5)
    val router: ActorRef[String] = context.spawn(routingBehavior, "test-pool")
    (0 to 10).foreach { n =>
      router ! "hi, there"
    }
    Behaviors.empty
  }
}
