package logging

import akka.actor.testkit.typed.scaladsl.ScalaTestWithActorTestKit
import akka.actor.testkit.typed.scaladsl.LoggingTestKit
import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpecLike
import akka.actor.typed.scaladsl.Behaviors
import akka.actor.typed.Behavior
import common.SimplifiedManager
import org.slf4j.event.Level

class AsyncLogSpec extends ScalaTestWithActorTestKit with AnyWordSpecLike with Matchers {

  "a Simplified Manager" must {
    "be able to log It's done" in {
      val manager = testKit.spawn(SimplifiedManager(), "manager")

      LoggingTestKit.info("It's done").expect {
        manager ! SimplifiedManager.Log
      }
    }
  }

  "a simple behavior" must {
    "log messages to dead letters" in {
      val behavior: Behavior[String] = Behaviors.stopped
      val awesomeNinja               = spawn(behavior, "awesomeNinja")

      LoggingTestKit.empty
        .withLogLevel(Level.INFO)
        .withMessageRegex(".*Message.*to.*awesomeNinja.*was not delivered.*2.*dead letters encountered")
        .expect {
          awesomeNinja ! "Hello"
          awesomeNinja ! "Hello"

        }
    }
  }

}
