package simplequestion

import akka.actor.typed.ActorSystem

object SimpleQuestionApp extends App {
  val guardian: ActorSystem[Guardian.Command] = ActorSystem(Guardian(), "example-ask-without-content")

  guardian ! Guardian.Start(List("text-a", "text-b", "text-c"))
  print("Press Enter to exit...")
  scala.io.StdIn.readLine()
  guardian.terminate()

}
