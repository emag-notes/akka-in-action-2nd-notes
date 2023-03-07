import akka.actor.typed.ActorSystem
import org.slf4j.LoggerFactory

object WalletApp extends App {

  private val logger = LoggerFactory.getLogger("WalletApp")

  private val guardian: ActorSystem[Int] = ActorSystem(Wallet(), "wallet")

  guardian ! 1
  guardian ! 10

  logger.info("Press ENTER to terminate")
  scala.io.StdIn.readLine()
  guardian.terminate()
}
