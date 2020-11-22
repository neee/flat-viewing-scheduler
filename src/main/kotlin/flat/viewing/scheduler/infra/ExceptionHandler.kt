package flat.viewing.scheduler.infra

import io.ktor.application.call
import io.ktor.features.BadRequestException
import io.ktor.features.StatusPages
import io.ktor.http.HttpStatusCode
import io.ktor.response.respond

fun StatusPages.Configuration.handleExceptions() {
    exception<RuntimeException> {
        call.respond(
            HttpStatusCode.InternalServerError,
            Error(HttpStatusCode.InternalServerError.value, it.message)
        )
    }
    exception<BadRequestException> {
        call.respond(HttpStatusCode.BadRequest, Error(HttpStatusCode.BadRequest.value, it.message))
    }
}

data class Error(
    val code: Int,
    val message: String?
)
