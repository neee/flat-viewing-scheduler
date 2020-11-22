package flat.viewing.scheduler.api

import io.ktor.application.ApplicationCall
import io.ktor.application.call
import io.ktor.features.BadRequestException
import io.ktor.request.header
import io.ktor.util.pipeline.PipelineContext

enum class Header(val value: String) {
    X_USER_ID("X-User-Id"),
    /**/;
}

fun PipelineContext<Unit, ApplicationCall>.headerValue(header: Header): String = headerValue(header.value)

fun PipelineContext<Unit, ApplicationCall>.headerValue(headerName: String): String {
    return call.request.header(headerName)
        ?: throw BadRequestException("Required header: \"$headerName\" not found")
}
