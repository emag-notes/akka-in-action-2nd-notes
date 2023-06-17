package faulttolerance1

import akka.actor.typed.{ActorRef, Terminated}
import akka.actor.typed.scaladsl.Behaviors

object LogProcessingGuardian {

  def apply(sources: Vector[String], databaseUrl: String) = Behaviors.setup[Nothing] { context =>
    sources.foreach { source =>
      val dbWriter: ActorRef[DbWriter.Command] = context.spawnAnonymous(DbWriter(databaseUrl))
      // ログプロセッサを増やしたほうが良さそう
      val logProcessor: ActorRef[LogProcessor.Command] = context.spawnAnonymous(LogProcessor(dbWriter))
      val fileWatcher: ActorRef[FileWatcher.Command] = context.spawnAnonymous(FileWatcher(source, logProcessor))
      context.watch(fileWatcher)
    }
    Behaviors.receiveMessage[Nothing] {
      _: Any =>
        Behaviors.ignore
    }.receiveSignal {
      case (context, Terminated(actorRef)) =>
        // 実行中のファイルウォッチャーの存在を確認する
        Behaviors.same
    }
  }
}