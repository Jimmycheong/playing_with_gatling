package com.jimmycheong.github.project

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import scala.concurrent.duration._

class GatlingSimulation extends Simulation {

  val httpProtocol = http
    .baseUrl("http://localhost:8080")
    .acceptHeader(
      "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8")
    .doNotTrackHeader("1")
    .acceptLanguageHeader("en-US,en;q=0.5")
    .acceptEncodingHeader("gzip, deflate")
    .userAgentHeader(
      "Mozilla/5.0 (Windows NT 5.1; rv:31.0) Gecko/20100101 Firefox/31.0")

  val scenario_1 =
    scenario("getRequests")
      .exec(
        http("get titles")
          .get("/titles"))

  val scenario_2 =
    scenario("postRequests")
      .exec(
        http("posting items")
          .post("/create-order")
          .body(StringBody("""{"items":[]}"""))
          .asJson
      )

  setUp(
//    scenario_1.inject(atOnceUsers(3)),
    scenario_2.inject(atOnceUsers(1))
  ).protocols(httpProtocol)

}
