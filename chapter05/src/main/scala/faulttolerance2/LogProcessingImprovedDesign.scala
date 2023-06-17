package faulttolerance2

import akka.actor.typed.ActorSystem

object LogProcessingImprovedDesign extends App {
  val directories = Vector("file:///source1/", "file:///source2/")

  val guardian = ActorSystem[Nothing](LogProcessingGuardian(directories), "log-processing-app")

}
