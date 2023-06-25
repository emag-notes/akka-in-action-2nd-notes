package routers

import akka.actor.typed.{ActorRef, Behavior}
import akka.actor.typed.receptionist.ServiceKey
import akka.actor.typed.scaladsl.Behaviors

object PhotoProcessorSketch {

  val key = ServiceKey[String]("photo-processor-key")

  sealed trait Command
  final case class File(location: String, camera: ActorRef[Camera.Photo]) extends Command
  final case object Done extends Command

  def apply(): Behavior[Command] = ready()
  def ready(): Behavior[Command] = Behaviors.receiveMessage[Command] {
    case _: File =>
      busy()
  }
  def busy(): Behavior[Command] = Behaviors.receiveMessage[Command] {
    case File(location, camera) =>
      camera ! Camera.Photo(location)
      Behaviors.same
    case Done =>
      ready()
  }
}
