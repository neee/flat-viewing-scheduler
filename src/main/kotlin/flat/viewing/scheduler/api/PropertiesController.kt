package flat.viewing.scheduler.api

import flat.viewing.scheduler.domain.properties.PropertiesRepository
import flat.viewing.scheduler.domain.properties.Property
import io.ktor.application.call
import io.ktor.http.HttpStatusCode
import io.ktor.request.receive
import io.ktor.response.respond
import io.ktor.routing.Routing
import io.ktor.routing.get
import io.ktor.routing.post

fun Routing.properties(propertiesRepository: PropertiesRepository) {
    get("/properties") {
        call.respond(propertiesRepository.getAll())
    }
    post("/properties") {
        val property = call.receive<Property>()
        propertiesRepository.add(property)
        call.respond(HttpStatusCode.OK)
    }
}
