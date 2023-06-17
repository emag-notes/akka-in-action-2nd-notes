package faulttolerance1

import akka.actor.typed.scaladsl.Behaviors
import akka.actor.typed.{ ActorRef, Behavior, SupervisorStrategy }
import faulttolerance1.exception.CorruptedFileException

import java.io.File

object LogProcessor {

  sealed trait Command
  final case class LogFile(file: File) extends Command

  def apply(dbWriter: ActorRef[DbWriter.Command]): Behavior[Command] =
    Behaviors
      .supervise {
        Behaviors.receiveMessage[Command] {
          case LogFile(file) => ???
          // ファイルをパースし、各行を dbWriter に送る
        }
      }
      .onFailure[CorruptedFileException](SupervisorStrategy.resume)
}
