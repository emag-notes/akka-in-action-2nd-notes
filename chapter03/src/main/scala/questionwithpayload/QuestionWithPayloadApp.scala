package questionwithpayload

import akka.actor.typed.ActorSystem
import org.slf4j.LoggerFactory

object QuestionWithPayloadApp extends App {

  private val logger = LoggerFactory.getLogger("QuestionWithPayloadApp")

  private val guardian: ActorSystem[Guardian.Command] = ActorSystem(Guardian(), "example-ask-with-content")

  guardian ! Guardian.Start(List("text-a", "text-b", "text-c"))

  logger.info("Press ENTER to terminate")
  scala.io.StdIn.readLine()
  guardian.terminate()
}
