package simplequestion

import akka.actor.typed.ActorSystem
import org.slf4j.LoggerFactory

object SimpleQuestionApp extends App {

  private val logger = LoggerFactory.getLogger("SimpleQuestionApp")

  private val guardian: ActorSystem[Guardian.Command] = ActorSystem(Guardian(), "example-ask-without-content")

  guardian ! Guardian.Start(List("text-a", "text-b", "text-c"))

  logger.info("Press ENTER to terminate")
  scala.io.StdIn.readLine()
  guardian.terminate()
}
