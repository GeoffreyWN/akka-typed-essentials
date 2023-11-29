import akka.actor.typed.Behavior
import akka.actor.typed.scaladsl.Behaviors

import scala.concurrent.duration.DurationInt

object WalletTimer {
  sealed trait Command

  final case class Increase(amount: Int) extends Command

  final case class Deactivate(seconds: Int) extends Command

  private final case object Activate extends Command

  def apply(): Behavior[Command] = activated(0)

  def activated(total: Int): Behavior[Command] =
    Behaviors.receive { (context, message) =>
      Behaviors.withTimers { timers =>
        message match {
          case Increase(amount) =>
            val current = total + amount
            context.log.info(s"increasing to $current")
            activated(current)
          case Deactivate(t) =>
            timers.startSingleTimer(Activate, t.second)
            deactivated(total)
          case Activate => Behaviors.same
        }
      }
    }

  def deactivated(total: Int): Behavior[Command] = {
    Behaviors.receive { (context, message) =>
      message match {
        case Increase(_) =>
          context.log.info(s"Increase failed. Wallet is already deactivated")
          Behaviors.same
        case Deactivate(_) =>
          context.log.info(s"Wallet is already deactivated")
          Behaviors.same
        case Activate =>
          context.log.info(s"Activating wallet...")
          activated(total)
      }
    }
  }

}





