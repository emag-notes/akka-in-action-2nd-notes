package errorkernel

import akka.actor.typed.ActorSystem
import org.slf4j.LoggerFactory

object ErrorKernelApp extends App {

  private val logger = LoggerFactory.getLogger("ErrorKernelApp")

  private val guardian: ActorSystem[Guardian.Command] = ActorSystem(Guardian(), "error-kernel")

  guardian ! Guardian.Start(List("-one-", "--two--"))

  logger.info("Press ENTER to terminate")
  scala.io.StdIn.readLine()
  guardian.terminate()
}
