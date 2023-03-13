import akka.actor.typed.Behavior
import akka.actor.typed.scaladsl.Behaviors

object SimplifiedWorker {
  def apply(): Behavior[String] = Behaviors.ignore[String]
}
