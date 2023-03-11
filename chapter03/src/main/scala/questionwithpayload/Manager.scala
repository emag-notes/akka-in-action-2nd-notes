package questionwithpayload

import akka.actor.typed.{ ActorRef, Behavior }
import akka.actor.typed.scaladsl.Behaviors
import akka.util.Timeout

import scala.concurrent.duration.SECONDS
import scala.util.{ Failure, Success }

object Manager {

  sealed trait Command
  final case class Delegate(texts: List[String]) extends Command
  private final case class Report(description: String) extends Command

  def apply(): Behavior[Command] = Behaviors.setup { context =>
    implicit val timeout: Timeout = Timeout(3, SECONDS)

    def auxCreateRequest(text: String)(replyTo: ActorRef[Worker.Response]): Worker.Parse =
      Worker.Parse(text, replyTo)

    Behaviors.receiveMessage {
      case Delegate(texts) =>
        texts.foreach { text =>
          val worker = context.spawn(Worker(), s"worker-$text")
          context.ask(worker, auxCreateRequest(text)) {
            case Success(Worker.Done) =>
              Report(s"$text read by ${worker.path.name}")
            case Failure(ex) =>
              Report(s"parsing '$text' has failed with [${ex.getMessage}]'")
          }
        }
        Behaviors.same
      case Report(description) =>
        context.log.info(description)
        Behaviors.same
    }
  }
}
