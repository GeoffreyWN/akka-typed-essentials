package common

import akka.actor.testkit.typed.CapturedLogEvent
import akka.actor.testkit.typed.Effect.{ NoEffects, Scheduled, Spawned }
import akka.actor.testkit.typed.scaladsl.LoggingTestKit
import akka.actor.testkit.typed.scaladsl.{ ActorTestKit, BehaviorTestKit, ScalaTestWithActorTestKit, TestInbox }
import akka.actor.typed.scaladsl.Behaviors
import akka.actor.typed.{ ActorRef, Behavior }
import org.scalatest.BeforeAndAfterAll
import org.scalatest.exceptions.TestFailedException
import org.scalatest.matchers.must.Matchers
import org.scalatest.matchers.should.Matchers.convertToAnyShouldWrapper
import org.scalatest.wordspec.{ AnyWordSpec, AnyWordSpecLike }
import org.slf4j.event.Level

import scala.concurrent.duration.DurationInt

object SimplifiedManager {
  sealed trait Command
  final case class CreateChild(name: String) extends Command
  final case class Forward(message: String, sendTo: ActorRef[String]) extends Command
  final case object ScheduleLog extends Command
  final case object Log extends Command

  def apply(): Behaviors.Receive[Command] =
    Behaviors.receive { (context, message) =>
      message match {
        case CreateChild(name)     =>
          context.spawn(SimplifiedWorker(), name)
          Behaviors.same
        case Forward(text, sendTo) =>
          sendTo ! text
          Behaviors.same
        case ScheduleLog           =>
          context.scheduleOnce(3.seconds, context.self, Log)
          Behaviors.same
        case Log                   =>
          context.log.info(s"It's done")
          Behaviors.same
      }
    }

}

class SyncTestingSpec extends AnyWordSpec with Matchers {
  "Typed actor synchronous testing" must {
    "spawning takes place" in {
      val testKit = BehaviorTestKit(SimplifiedManager())
      testKit.expectEffect(NoEffects)
      testKit.run(SimplifiedManager.CreateChild("coolkid"))
      testKit.expectEffect(Spawned(SimplifiedWorker(), "coolkid"))
    }

    "actor gets forwarded message from manager" in {
      val testKit = BehaviorTestKit(SimplifiedManager())
      val probe   = TestInbox[String]()
      testKit.run(SimplifiedManager.Forward("hello", probe.ref))
    }

    "record the log" in {
      val testKit = BehaviorTestKit(SimplifiedManager())
      testKit.run(SimplifiedManager.Log)
      testKit.logEntries() shouldBe Seq(CapturedLogEvent(Level.INFO, "It's done"))
    }

    "failing to schedule a message. BehaviorTestKit can't deal with scheduling" in {
      intercept[TestFailedException] {
        val testKit = BehaviorTestKit(SimplifiedManager())
        testKit.run(SimplifiedManager.ScheduleLog)
        testKit.expectEffect(Scheduled(1.seconds, testKit.ref, SimplifiedManager.Log))
        testKit.logEntries() shouldBe Seq(CapturedLogEvent(Level.INFO, "it's done"))
      }
    }
  }
}

class AsyncForwardSpec extends AnyWordSpec with BeforeAndAfterAll with Matchers {

  val testKit: ActorTestKit = ActorTestKit()

  override protected def afterAll(): Unit = testKit.shutdownTestKit()

  "A simplified Manager" must {

    "actor gets forwarded message from manager" in {
      val manager = testKit.spawn(SimplifiedManager())
      val probe   = testKit.createTestProbe[String]()

      manager ! SimplifiedManager.Forward("message-to-parse", probe.ref)

      probe.expectMessage("message-to-parse")
    }

  }

  "a monitor " must {
    "intercept the messages" in {
      val probe             = testKit.createTestProbe[String]
      val behaviorUnderTest = Behaviors.receiveMessage[String] { _ => Behaviors.ignore }
      val behaviorMonitored = Behaviors.monitor(probe.ref, behaviorUnderTest)

      val actor = testKit.spawn(behaviorMonitored)

      actor ! "checking"

      probe.expectMessage("checking")

    }
  }

}


