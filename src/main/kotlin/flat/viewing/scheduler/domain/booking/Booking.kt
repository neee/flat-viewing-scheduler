package flat.viewing.scheduler.domain.booking

import java.time.Instant

data class Booking(
    val id: String,
    val propertyId: String,
    val newTenantId: String,
    val bookStart: Instant,
    val bookEnd: Instant,
    val bookingStatus: BookingStatus
) {

    fun updateStatus(bookingStatus: BookingStatus): Booking {
        return Booking(
            this.id,
            this.propertyId,
            this.newTenantId,
            this.bookStart,
            this.bookEnd,
            bookingStatus
        )
    }
}
