package simplequestion

import akka.actor.typed.Behavior
import akka.actor.typed.scaladsl.Behaviors

object Guardian {
  sealed trait Command

  final case class Start(texts: List[String]) extends Command

  def apply(): Behavior[Command] =
    Behaviors.setup { context =>
      val manager = context.spawn(Manager(), "manager-1")
      Behaviors.receiveMessage { case Start(texts) =>
        manager ! Manager.Delegate(texts)
        Behaviors.same
      }
    }

}
