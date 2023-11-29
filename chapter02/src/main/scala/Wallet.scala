import akka.actor.typed.Behavior
import akka.actor.typed.scaladsl.Behaviors


object Wallet {
  def apply(): Behavior[Double] =
    Behaviors.receive { (context, message) =>
      context.log.info(s"received Ksh $message ")
      Behaviors.same

    }

}
