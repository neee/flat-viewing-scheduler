package flat.viewing.scheduler.api

import flat.viewing.scheduler.api.Header.X_USER_ID
import flat.viewing.scheduler.domain.booking.BookingManager
import flat.viewing.scheduler.domain.booking.BookingRepository
import io.ktor.application.call
import io.ktor.http.HttpStatusCode
import io.ktor.request.receive
import io.ktor.response.respond
import io.ktor.routing.Routing
import io.ktor.routing.get
import io.ktor.routing.patch
import io.ktor.routing.post
import java.time.Instant

fun Routing.bookings(
    bookingRepository: BookingRepository,
    bookingManager: BookingManager
) {
    get("/bookings") {
        call.respond(bookingRepository.getAll())
    }
    get("/bookings/{bookingId}") {
        val bookingId = parameterValue(Parameter.BOOKING_ID)
        call.respond(bookingRepository.get(bookingId))
    }
    post("/bookings") {
        val newTenantId = headerValue(X_USER_ID)
        val body = call.receive<BookingRequest>()
        bookingManager.book(newTenantId, body.propertyId, Instant.parse(body.bookStart))
        call.respond(HttpStatusCode.OK)
    }
    patch("/bookings/{bookingId}/approve") {
        val currentTenantId = headerValue(X_USER_ID)
        val bookingId = parameterValue(Parameter.BOOKING_ID)
        bookingManager.approve(currentTenantId, bookingId)
        call.respond(HttpStatusCode.OK)
    }
    patch("/bookings/{bookingId}/reject") {
        val currentTenantId = headerValue(X_USER_ID)
        val bookingId = parameterValue(Parameter.BOOKING_ID)
        bookingManager.reject(currentTenantId, bookingId)
        call.respond(HttpStatusCode.OK)
    }
    patch("/bookings/{bookingId}/delete") {
        val newTenantId = headerValue(X_USER_ID)
        val bookingId = parameterValue(Parameter.BOOKING_ID)
        bookingManager.delete(newTenantId, bookingId)
        call.respond(HttpStatusCode.OK)
    }
}

data class BookingRequest(
    val propertyId: String,
    val bookStart: String
)
