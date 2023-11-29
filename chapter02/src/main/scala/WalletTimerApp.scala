import akka.actor.typed.ActorSystem

object WalletTimerApp extends App{
  val guardian = ActorSystem(WalletTimer(), "wallet-timer")

  guardian ! WalletTimer.Increase(10)
  guardian ! WalletTimer.Deactivate(3)
  guardian ! WalletTimer.Deactivate(3)


  print("Press Enter to exit...")
  scala.io.StdIn.readLine()
  guardian.terminate()

}
