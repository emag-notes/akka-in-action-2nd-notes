import akka.actor.typed.ActorSystem
import org.slf4j.LoggerFactory

import java.util.concurrent.TimeUnit

object WalletTimerApp extends App {

  private val logger = LoggerFactory.getLogger("WalletTimerApp")

  private val guardian: ActorSystem[WalletTimer.Command] = ActorSystem(WalletTimer(), "wallet-timer")

  guardian ! WalletTimer.Increase(1)
  guardian ! WalletTimer.Deactivate(3)
  guardian ! WalletTimer.Increase(1)
  guardian ! WalletTimer.Deactivate(3)

  logger.info("Press ENTER to terminate")
  scala.io.StdIn.readLine()
  guardian ! WalletTimer.Increase(1)
  guardian.terminate()
}
