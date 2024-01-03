package errorkernel

import akka.actor.typed.ActorSystem

object ErrorKernelApp extends App {

  val guardian = ActorSystem(Guardian(), "error-kernel")
  guardian ! Guardian.Start(List("-one-", "--two--"))

  print("Press Enter to exit...")
  scala.io.StdIn.readLine()
  guardian.terminate()

}
