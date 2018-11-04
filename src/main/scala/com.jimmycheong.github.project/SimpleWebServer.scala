package com.jimmycheong.github.project

import akka.Done
import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.marshalling.{Marshaller, ToResponseMarshallable, ToResponseMarshaller}
import akka.http.scaladsl.model._
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import akka.stream.ActorMaterializer
import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport._

import scala.concurrent.Future
import scala.io.StdIn

object SimpleWebServer {

  def main(args: Array[String]) {

    // needed to run the route
    implicit val system = ActorSystem()
    implicit val materializer = ActorMaterializer()
    // needed for the future map/flatmap in the end and future in fetchItem and saveOrder
    implicit val executionContext = system.dispatcher

    final case class Item(name: String, id: Long)
    final case class Order(items: List[Item])

    import spray.json.DefaultJsonProtocol._

    implicit val itemFormat = jsonFormat2(Item)
    implicit val orderFormat = jsonFormat1(Order)


    var orders: List[Item] = List(
      Item("hat", 1),
      Item("shirt", 2),
      Item("trousers", 3)
    )

    // (fake) async database query api
    def fetchItem(itemId: Long): Future[Option[Item]] = Future {
      orders.find(o => o.id == itemId)
    }
    def saveOrder(order: Order): Future[Done] = {
      orders = order match {
        case Order(items) => items ::: orders
        case _            => orders
      }
      Future { Done }
    }

    val route: Route =
      get {
        pathPrefix("item" / LongNumber) { id =>
          // there might be no item for a given id
          val maybeItem: Future[Option[Item]] = fetchItem(id)

          onSuccess(maybeItem) {
            case Some(item) => complete(item)
            case None       => complete(StatusCodes.NotFound)
          }
        }
      } ~
        post {
          path("create-order") {
            entity(as[Order]) { order =>
              println(s"order received: ${order.toString}")
              val saved: Future[Done] = saveOrder(order)
              onComplete(saved) { done =>
                println("reached")
                complete(ToResponseMarshallable("completed request!"))
              }
            }
          }
        }

    val bindingFuture = Http().bindAndHandle(route, "localhost", 8080)
    println(s"Server online at http://localhost:8080/\nPress RETURN to stop...")
    StdIn.readLine() // let it run until user presses return
    bindingFuture
      .flatMap(_.unbind()) // trigger unbinding from the port
      .onComplete(_ ⇒ system.terminate()) // and shutdown when done
  }

}
