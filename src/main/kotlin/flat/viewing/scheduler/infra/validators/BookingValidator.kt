package flat.viewing.scheduler.infra.validators

import flat.viewing.scheduler.domain.booking.BookingRepository

class BookingValidator(private val bookingRepository: BookingRepository) {

    suspend fun validate(bookingId: String) {
        bookingRepository.get(bookingId)
    }
}
