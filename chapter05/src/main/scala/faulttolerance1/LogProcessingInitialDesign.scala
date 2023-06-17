package faulttolerance1

import akka.actor.typed.ActorSystem

object LogProcessingInitialDesign extends App {
  private val sources = Vector("file:///source1/", "file:///source2/")
  private val databaseUrl = "http://mydatabase1"

  val guardian = ActorSystem[Nothing](LogProcessingGuardian(sources, databaseUrl), "log-processing-app")
}
