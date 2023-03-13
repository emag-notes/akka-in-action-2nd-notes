import akka.actor.testkit.typed.scaladsl.{ LogCapturing, LoggingTestKit, ScalaTestWithActorTestKit }
import akka.actor.typed.Behavior
import akka.actor.typed.scaladsl.Behaviors
import org.scalatest.wordspec.AnyWordSpecLike
import org.slf4j.event.Level

class AsyncLogSpec extends ScalaTestWithActorTestKit with AnyWordSpecLike with LogCapturing {
  "a Simplified Manager" should {
    "be able to log 'it's done'" in {
      val manager = testKit.spawn(SimplifiedManager(), "manager")
      LoggingTestKit.info("it's done").expect {
        manager ! SimplifiedManager.Log
      }
    }
  }

  "a simple behavior" should {
    val behavior: Behavior[String] = Behaviors.stopped
    val carl = spawn(behavior, "carl")
    LoggingTestKit.empty
      .withLogLevel(Level.INFO)
      .withMessageRegex(".*Message.*to.*carl.*was not delivered.*2.*dead letters encountered")
      .expect {
        carl ! "Hello"
        carl ! "Hello"
      }
  }
}
