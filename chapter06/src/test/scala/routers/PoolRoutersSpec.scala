package routers

import akka.actor.testkit.typed.scaladsl.{LogCapturing, ScalaTestWithActorTestKit, TestProbe}
import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpecLike

class PoolRoutersSpec extends ScalaTestWithActorTestKit with AnyWordSpecLike with Matchers with LogCapturing {

  "a ppl router" should {
    "send messages in round-robing fashion" in {
      val probe = TestProbe[String]
      val worker = Worker(probe.ref)
      spawn(Manager(worker), "round-robin")

      probe.expectMessage("hi")
      probe.receiveMessages(10)
    }
    "Broadcast, sending each message to all routees" in {
      val probe = TestProbe[String]
      val worker = Worker(probe.ref)
      spawn(BroadcastingManager(worker), "broadcasting")

      probe.expectMessage("hi, there")
      probe.receiveMessages(43)
    }
    "ConsistentHashing send messages to only one" in {
      val probe = TestProbe[String]
      val worker = Worker(probe.ref)
      spawn(Manager(worker), "constant-hashing")

      probe.expectMessage("hi")
      probe.receiveMessages(10)
    }
  }
}
