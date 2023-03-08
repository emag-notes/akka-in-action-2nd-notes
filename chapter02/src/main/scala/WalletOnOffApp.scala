import akka.actor.typed.ActorSystem
import org.slf4j.LoggerFactory

object WalletOnOffApp extends App {

  private val logger = LoggerFactory.getLogger("WalletOnOffApp")

  private val guardian: ActorSystem[WalletOnOff.Command] = ActorSystem(WalletOnOff(), "wallet-on-off")

  guardian ! WalletOnOff.Increase(1)
  guardian ! WalletOnOff.Deactivate
  guardian ! WalletOnOff.Increase(1)
  guardian ! WalletOnOff.Activate
  guardian ! WalletOnOff.Increase(1)

  logger.info("Press ENTER to terminate")
  scala.io.StdIn.readLine()
  guardian.terminate()
}
