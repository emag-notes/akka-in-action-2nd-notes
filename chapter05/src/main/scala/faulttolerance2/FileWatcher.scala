package faulttolerance2

import akka.actor.typed.{ Behavior, SupervisorStrategy, Terminated }
import akka.actor.typed.scaladsl.Behaviors
import faulttolerance2.exception.ClosedWatchServiceException

import java.io.File

object FileWatcher extends FileListeningAbilities {
  sealed trait Command
  final case class NewFile(file: File, timeAdded: Long) extends Command
  final case class FileModified(file: File, timeAdded: Long) extends Command

  def apply(directory: String): Behavior[Command] =
    Behaviors
      .supervise {
        Behaviors.setup[Command] { context =>
          Behaviors
            .receiveMessage[Command] {
              case NewFile(file, timeAdded)      => ???
              case FileModified(file, timeAdded) => ???
            }
            .receiveSignal {
              case (context, Terminated(ref)) => ???
            }
        }
      }
      .onFailure[ClosedWatchServiceException](SupervisorStrategy.restart)

}
