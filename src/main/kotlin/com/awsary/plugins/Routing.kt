package com.awsary.plugins

import com.awsary.client.Database
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.application.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.server.plugins.openapi.*
import io.ktor.server.resources.Resources
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Application.configureRouting() {
    install(ContentNegotiation) {
        json()
    }

    install(Resources)

    routing {
        openAPI(path = "openapi")
        awsItemsRouting()
        defaultRouting()
    }
}

fun Route.defaultRouting() {
    route("/"){
        get {
            call.respond(HttpStatusCode.OK, "Hello I am on")
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
                call.respond(HttpStatusCode.InternalServerError, e)
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
                call.respond(HttpStatusCode.InternalServerError, e)
            }
        }
    }
}
