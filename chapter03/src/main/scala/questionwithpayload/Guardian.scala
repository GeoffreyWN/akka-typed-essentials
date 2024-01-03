package questionwithpayload

import akka.actor.typed.Behavior
import akka.actor.typed.scaladsl.Behaviors

object Guardian {
  sealed trait Command

  final case object Start extends Command

  def apply(): Behavior[Command] =
    Behaviors.setup { context =>
      val manager = context.spawn(Manager(), "manager-1")
      Behaviors.receiveMessage { message =>
        manager ! Manager.Delegate(List("text-a", "text-b", "text-c"))
        Behaviors.same
      }
    }

}
