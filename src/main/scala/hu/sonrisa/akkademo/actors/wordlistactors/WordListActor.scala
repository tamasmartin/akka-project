package hu.sonrisa.akkademo.actors.wordlistactors

import akka.actor.{Actor, ActorRef, Props}
import hu.sonrisa.akkademo.model.{ProcessStringMsg, StartProcessFileMsg}

class WordListActor(filename: String, wordList: scala.collection.mutable.Map[String, Integer]) extends Actor {

  private var fileSender: Option[ActorRef] = None
  private var totalLines = 0
  private var linesProcessed = 0
//
//  override def preStart(): Unit = log.info("WordCounterActor started")
//
//  override def postStop(): Unit = log.info("WordCounterActor stopped")

  def receive = {

    case _: StartProcessFileMsg => {
      fileSender = Some(sender)
      import scala.io.Source._

      fromFile(filename).getLines().foreach(line => {
        totalLines = totalLines + 1
        context.actorOf(Props[LineProcessor]) ! ProcessStringMsg(line)
      })
    }
    case p : LineProcessed =>
      p.words.foreach(p => {
        wordList.get(p._1) match {
          case Some(x) => {
            wordList += (p._1 -> (x + 1))
          }
          case None => {
            wordList += (p._1 -> 1)
          }
        }
      })
      linesProcessed = linesProcessed + 1
      if (linesProcessed == totalLines) {
        fileSender.get.tell(wordList, self)
      }

  }
}
