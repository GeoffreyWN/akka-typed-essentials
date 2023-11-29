import akka.actor.typed.ActorSystem

object WalletOnOffApp extends App{
val guardian = ActorSystem(WalletOnOff(), "wallet-on-off")

  guardian ! WalletOnOff.Increase(10)
  guardian ! WalletOnOff.Deactivate
  guardian ! WalletOnOff.Increase(10)
  guardian ! WalletOnOff.Activate
  guardian ! WalletOnOff.Increase(10)

  print("Press Enter to exit...")
  scala.io.StdIn.readLine()
  guardian.terminate()

}
