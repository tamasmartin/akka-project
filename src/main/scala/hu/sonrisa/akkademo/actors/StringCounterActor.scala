package hu.sonrisa.akkademo.actors

import akka.actor.{Actor, ActorLogging}
import hu.sonrisa.akkademo.model.ProcessStringMsg

class StringCounterActor extends Actor with ActorLogging {

  override def receive = {
    case ProcessStringMsg(line) => {
      val counts = line.split(" ").length
      sender ! StringProcessedMsg(counts)
    }
    case _ => log.error("Unrecognised command")
  }
}
