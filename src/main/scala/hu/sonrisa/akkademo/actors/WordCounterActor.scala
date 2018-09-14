package hu.sonrisa.akkademo.actors

import akka.actor.{Actor, ActorLogging, ActorRef, Props}
import hu.sonrisa.akkademo.model.{ProcessStringMsg, StartProcessFileMsg}

class WordCounterActor(filename: String) extends Actor with ActorLogging {

  private var fileSender: Option[ActorRef] = None
  private var result = 0
  private var totalLines = 0
  private var linesProcessed = 0

  override def preStart(): Unit = log.info("WordCounterActor started")

  override def postStop(): Unit = log.info("WordCounterActor stopped")

  def receive = {

    case _: StartProcessFileMsg => {
      fileSender = Some(sender)
      import scala.io.Source._

      fromFile(filename).getLines().foreach(line => {
        totalLines = totalLines + 1
        context.actorOf(Props[StringCounterActor]) ! ProcessStringMsg(line)
      })
    }
    case StringProcessedMsg(wordC) => {
      result = result + wordC
      linesProcessed = linesProcessed + 1
      if (linesProcessed == totalLines) {
        fileSender.get.tell(result, self)
      }
    }

  }
}
