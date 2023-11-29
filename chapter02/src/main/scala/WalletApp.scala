import akka.actor.typed.ActorSystem

object WalletApp extends App {
  val guardian: ActorSystem[Double] = ActorSystem(Wallet(), "wallet")

  guardian ! 1000.00
  guardian ! 2000.00

  print("Press Enter to exit...")
  scala.io.StdIn.readLine()
  guardian.terminate()

}
