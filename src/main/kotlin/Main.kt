import io.ktor.application.*
import io.ktor.client.*
import io.ktor.client.features.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.response.*
import io.ktor.routing.*
import io.ktor.server.cio.*
import io.ktor.server.engine.*
import kotlinx.coroutines.runBlocking

object Main {

  @JvmStatic
  fun main(args: Array<String>) {

    val httpServer =
      embeddedServer(CIO, port = 8080) {
        routing {
          get("/good") {
            call.respondText("I am here", ContentType.Text.Plain)
          }
        }
      }

    httpServer.start(false)

    runBlocking {
      HttpClient { install(HttpTimeout) }.use { client ->
        val response = client.request<HttpStatement>("http://0.0.0.0:8080/good") {
          method = HttpMethod.Get
        }.execute()
        println("/good: ${response.status}")
      }

      HttpClient { install(HttpTimeout) }.use { client ->
        val response = client.request<HttpStatement>("http://0.0.0.0:8080/bad") {
          method = HttpMethod.Get
        }.execute()
        println("/bad: ${response.status}")
      }
    }

    httpServer.stop(0, 0)
  }
}