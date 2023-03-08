import WalletApp.{guardian, logger}
import akka.actor.typed.ActorSystem
import org.slf4j.LoggerFactory

object WalletStateApp extends App {

  private val logger = LoggerFactory.getLogger("WalletStateApp")

  private val guardian: ActorSystem[WalletState.Command] = ActorSystem(WalletState(0, 2), "wallet-state")

  guardian ! WalletState.Increase(1)
  guardian ! WalletState.Increase(1)
  guardian ! WalletState.Increase(1)

  logger.info("Press ENTER to terminate")
  scala.io.StdIn.readLine()
  guardian.terminate()
}
