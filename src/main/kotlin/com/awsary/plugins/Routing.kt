package com.awsary.plugins

import com.awsary.client.Database
import io.ktor.http.*
import io.ktor.resources.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.application.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.server.plugins.openapi.*
import io.ktor.server.resources.*
import io.ktor.server.resources.Resources
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlinx.serialization.Serializable

fun Application.configureRouting() {
    install(ContentNegotiation) {
        json()
    }

    install(Resources)

    routing {
        openAPI(path = "openapi")
        awsItemsRouting()
        get("/") {

            try {
                val dataBase = Database()
                val items = dataBase.getItems()

                call.respond(HttpStatusCode.OK, items)
            } catch (e: Exception) {
                println(e)
            }

            call.respondText("HI")
        }
    }
}

fun Route.awsItemsRouting() {

    val dataBase = Database()

    route("/items") {
        get {
            try {
                val items = dataBase.getItems()
                call.respond(HttpStatusCode.OK, items)
            } catch (e: Exception) {
                println(e)
            }
        }
        get("{id?}") {

            val id = call.parameters["id"] ?: return@get call.respondText(
                "Missing id",
                status = HttpStatusCode.BadRequest
            )

            try {
                val item = dataBase.getItem(id)
                item?.let {
                    call.respond(HttpStatusCode.OK, item)
                }
            } catch (e: Exception) {
                println(e)
            }
        }
    }

}
