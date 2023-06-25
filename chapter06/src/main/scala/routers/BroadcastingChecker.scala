package routers

import akka.actor.typed.Behavior
import akka.actor.typed.scaladsl.{ Behaviors, PoolRouter, Routers }

object BroadcastingChecker {

  def apply(behavior: Behavior[HighWayPatrol.Command]) = Behaviors.setup[Unit] { context =>
    val poolSize = 4
    val dataCheckerRouter: PoolRouter[HighWayPatrol.Command] =
      Routers.pool(poolSize)(behavior).withBroadcastPredicate(_.isInstanceOf[HighWayPatrol.Violation])
    ???
  }
}
