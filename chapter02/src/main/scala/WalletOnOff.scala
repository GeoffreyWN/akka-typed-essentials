import akka.actor.typed.Behavior
import akka.actor.typed.scaladsl.Behaviors

object WalletOnOff {
  sealed trait Command

  final case class Increase(amount: Int) extends Command
  final case object Activate extends Command
  final case object Deactivate extends Command

  def apply(): Behavior[Command] = activated(0)

  def activated(total: Int): Behavior[Command] =
    Behaviors.receive { (context, message) =>
      message match {
        case Increase(amount) =>
          val current = total + amount
          context.log.info(s"Increasing to $current")
          activated(current)
        case Deactivate       =>
          context.log.info(s"Deactivating wallet...")
          deactivated(total)
        case Activate         => Behaviors.same
      }

    }

  def deactivated(total: Int): Behavior[Command] =
    Behaviors.receive { (context, message) =>
      message match {
        case Increase(_) =>
          context.log.info(s"Increase operation not allowed. Wallet is deactivated!")
          Behaviors.same
        case Deactivate  => Behaviors.same
        case Activate    =>
          context.log.info(s"Activating wallet...")
          activated(total)
      }
    }

}
