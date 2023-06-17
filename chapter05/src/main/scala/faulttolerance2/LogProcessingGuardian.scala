package faulttolerance2

import akka.actor.typed.{ActorRef, Behavior, Terminated}
import akka.actor.typed.scaladsl.Behaviors

object LogProcessingGuardian {
  def apply(directories: Vector[String]): Behavior[Nothing] =
    Behaviors.setup[Nothing] { context =>
      directories.foreach { directory =>
        val fileWatcher: ActorRef[FileWatcher.Command] = context.spawnAnonymous(FileWatcher(directory))
        context.watch(fileWatcher)
      }
      Behaviors
        .receiveMessage[Nothing] { case _: Any => Behaviors.ignore }
        .receiveSignal { case (context, Terminated(actorRef)) => Behaviors.same }
    }
}
