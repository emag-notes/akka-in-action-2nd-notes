package faulttolerance1

import akka.actor.typed.{Behavior, PostStop, PreRestart, SupervisorStrategy}
import akka.actor.typed.scaladsl.Behaviors
import faulttolerance1.exception.{DbBrokenConnectionException, DbNodeDownException}

object DbWriter {

  sealed trait Command
  final case class Line(time: Long, message: String, messageType: String) extends Command

  def apply(databaseUrl: String): Behavior[Command] = supervisorStrategy {
    Behaviors.setup[Command] { context =>
      // databaseUrl に対応するコネクションを生成する
      Behaviors.receiveMessage[Command] {
        case Line(time, message, messageType) => ???
          // データベースに行を保存する
      }.receiveSignal {
        case (_, PostStop) => ???
          // コネクションを閉じる
        case (_, PreRestart) => ???
          // コネクションを閉じる
      }
    }
  }

  def supervisorStrategy(beh: Behavior[Command]): Behavior[Command] =
    Behaviors
      .supervise {
        Behaviors
          .supervise {
            beh
          }
          .onFailure[DbBrokenConnectionException](SupervisorStrategy.restart)
      }
      .onFailure[DbNodeDownException](SupervisorStrategy.stop)

}
