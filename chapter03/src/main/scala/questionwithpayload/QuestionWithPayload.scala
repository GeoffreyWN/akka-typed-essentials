package questionwithpayload

import akka.actor.typed.ActorSystem

object QuestionWithPayload extends App {
  val guardian: ActorSystem[Guardian.Command] = ActorSystem(Guardian(), "loaded-question")

  guardian ! Guardian.Start
  print("Press Enter to exit...")
  scala.io.StdIn.readLine()
  guardian.terminate()

}
