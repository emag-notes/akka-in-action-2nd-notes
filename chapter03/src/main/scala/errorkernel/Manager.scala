package errorkernel

import akka.actor.typed.Behavior
import akka.actor.typed.scaladsl.Behaviors

object Manager {

  sealed trait Command
  final case class Delegate(texts: List[String]) extends Command
  private final case class WorkerDoneAdapter(response: Worker.Response) extends Command

  def apply(): Behavior[Command] = Behaviors.setup { context =>
    val adapter = context.messageAdapter(WorkerDoneAdapter)

    Behaviors.receiveMessage {
      case Delegate(texts) =>
        texts.foreach { text =>
          val worker = context.spawn(Worker(), s"worker$text")
          context.log.info(s"sending text '$text' to worker")
          worker ! Worker.Parse(adapter, text)
        }
        Behaviors.same
      case WorkerDoneAdapter(Worker.Done(text)) =>
        context.log.info(s"text '$text' has been finished'")
        Behaviors.same
    }
  }
}
