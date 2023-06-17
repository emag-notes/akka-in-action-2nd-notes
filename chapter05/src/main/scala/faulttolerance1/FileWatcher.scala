package faulttolerance1

import akka.actor.typed.scaladsl.Behaviors
import akka.actor.typed.{ActorRef, Behavior, SupervisorStrategy}
import faulttolerance1.exception.CorruptedFileException

import java.io.File

object FileWatcher {

  sealed trait Command
  final case class NewFile(file: File, timeAdded: Long) extends Command

  def apply(directory: String, logProcessor: ActorRef[LogProcessor.Command]): Behavior[Command] =
    Behaviors.supervise {
      Behaviors.setup[Command] { context =>
        Behaviors.receiveMessage[Command] {
          case NewFile(file, _) => ???
            // ログプロセッサにファイルを送る
        }
      }
    }.onFailure[CorruptedFileException](SupervisorStrategy.res)
}
