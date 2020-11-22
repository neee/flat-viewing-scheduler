package flat.viewing.scheduler.domain.booking

import java.time.Instant

interface BookingRepository {

    suspend fun getAll(): Collection<Booking>

    suspend fun get(bookingId: String): Booking

    suspend fun add(tenantId: String, propertyId: String, bookStart: Instant, bookEnd: Instant): Booking

    suspend fun changeStatus(bookingId: String, newStatus: BookingStatus): Booking

    suspend fun delete(bookingId: String): Booking
}
