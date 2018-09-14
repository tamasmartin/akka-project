package hu.sonrisa.akkademo.actors.wordlistactors

import akka.actor.{Actor, ActorLogging}
import hu.sonrisa.akkademo.model.ProcessStringMsg

class LineProcessor extends Actor with ActorLogging {


  override def receive = {
    case ProcessStringMsg(line) => {

      var wordList = scala.collection.mutable.Map[String, Integer]()

      val words = line
        .replaceAll("[^A-Za-z\\s]", "")
        .split(" ")
        .filter(p => p.length() > 3)
        .toList
      words.foreach(p => {
        wordList.get(p) match {
          case Some(x) => wordList += (p -> (x + 1))
          case None => wordList += (p -> (1))
        }
      })
      sender ! LineProcessed(wordList.toMap)
    }
    case _ => log.error("Unrecognised command")
  }
}
