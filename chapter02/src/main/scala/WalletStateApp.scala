import akka.actor.typed.ActorSystem

object WalletStateApp extends App{
  val guardian = ActorSystem(WalletState(0, 10), "wallet-state")

  guardian ! WalletState.Increase(5)
  guardian ! WalletState.Increase(4)
  guardian ! WalletState.Decrease(20)


  println("Press enter to exit...")
  scala.io.StdIn.readLine()
  guardian.terminate()


}
