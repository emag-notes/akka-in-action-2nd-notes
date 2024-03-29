package routers

import akka.actor.testkit.typed.scaladsl.{LogCapturing, ScalaTestWithActorTestKit, TestProbe}
import akka.actor.typed.receptionist.Receptionist
import akka.actor.typed.scaladsl.Behaviors
import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpecLike

class GroupRouterSpec extends ScalaTestWithActorTestKit with AnyWordSpecLike with Matchers with LogCapturing {

  "a group router" should {
    "send messages to all photo processors registered. With no guarantee of fair distribution" in {
      val photoProcessor1= TestProbe[String]
      val pp1Monitor = Behaviors.monitor(photoProcessor1.ref, PhotoProcessor())

      val photoProcessor2 = TestProbe[String]
      val pp2Monitor = Behaviors.monitor(photoProcessor2.ref, PhotoProcessor())

      system.receptionist ! Receptionist.Register(PhotoProcessor.key, spawn(pp1Monitor))
      system.receptionist ! Receptionist.Register(PhotoProcessor.key, spawn(pp2Monitor))

      val camera = spawn(Camera())
      camera ! Camera.Photo("A")
      camera ! Camera.Photo("B")

      photoProcessor1.receiveMessages(1)
      photoProcessor2.receiveMessages(1)
    }

    "will send messages with same id to the same aggregator" in {
      val probe1 = TestProbe[Aggregator.Event]
      val probe2 = TestProbe[Aggregator.Event]

      spawn(Aggregator(forwardTo = probe1.ref), "aggregator1")
      spawn(Aggregator(forwardTo = probe2.ref), "aggregator2")

      val contentValidator = spawn(DataObfuscator(), "wa-1")
      val dataEnricher = spawn(DataEnricher(), "wb-1")

      contentValidator ! DataObfuscator.Message("123", "Text")
      dataEnricher ! DataEnricher.Message("123", "Text")
      contentValidator ! DataObfuscator.Message("123", "Text2")
      dataEnricher ! DataEnricher.Message("123", "Text2")

      probe1.receiveMessage().id shouldBe "123"
    }
  }
}
