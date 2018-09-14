package hu.sonrisa.akkademo

import akka.actor.{ActorSystem, PoisonPill, Props}
import akka.util.Timeout

import scala.concurrent.duration._
import hu.sonrisa.akkademo.model.StartProcessFileMsg
import akka.pattern.ask
import hu.sonrisa.akkademo.actors.wordlistactors.WordListActor

import scala.collection.immutable.ListMap
import scala.concurrent.Await

object SampleAkkaApp extends App {

  override def main(args: Array[String]): Unit = {

    val system = ActorSystem("WordCounterExample")
    val fileName = "d:\\Temp\\huge.txt"
    implicit val timeout = Timeout(925 seconds)
    var wordList = scala.collection.mutable.Map[String, Integer]()
    val actorRef = Props(new WordListActor(fileName, wordList))
    val actor = system.actorOf(actorRef)

    val future = actor ? new StartProcessFileMsg()
    val result = Await.result(future, timeout.duration)
    val theTop = ListMap(wordList.toSeq.sortWith(_._2 > _._2):_*)
    theTop.take(20).foreach(println(_))

    actor ! PoisonPill

    system.terminate()
  }

}
