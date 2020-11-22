package flat.viewing.scheduler.api

import io.ktor.application.ApplicationCall
import io.ktor.application.call
import io.ktor.features.BadRequestException
import io.ktor.util.pipeline.PipelineContext

enum class Parameter(val value: String) {
    BOOKING_ID("bookingId"),
    /**/;
}

fun PipelineContext<Unit, ApplicationCall>.parameterValue(parameter: Parameter): String = parameterValue(parameter.value)

fun PipelineContext<Unit, ApplicationCall>.parameterValue(parameterName: String): String {
    return call.parameters[parameterName] ?: throw BadRequestException("Parameter with name: \"$parameterName\" not found")
}
