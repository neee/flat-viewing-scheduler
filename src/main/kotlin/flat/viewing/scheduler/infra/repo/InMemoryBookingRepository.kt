package flat.viewing.scheduler.infra.repo

import flat.viewing.scheduler.domain.booking.BookingStatus
import flat.viewing.scheduler.domain.booking.Booking
import flat.viewing.scheduler.domain.booking.BookingRepository
import flat.viewing.scheduler.domain.exceptions.BookingNotFoundException
import flat.viewing.scheduler.domain.exceptions.TimeAlreadyBooked
import java.time.Instant
import java.util.UUID
import java.util.concurrent.ConcurrentHashMap

class InMemoryBookingRepository : BookingRepository {

    private val storage = ConcurrentHashMap<String, Booking>()

    override suspend fun getAll(): Collection<Booking> = storage.values

    override suspend fun get(bookingId: String): Booking {
        return storage[bookingId] ?: throw BookingNotFoundException("Booking with id: $bookingId not found")
    }

    override suspend fun add(tenantId: String, propertyId: String, bookStart: Instant, bookEnd: Instant): Booking {
        val isTimeBooked = storage.values.any {
            it.propertyId == propertyId
                && ((it.bookStart == bookStart || it.bookEnd == bookEnd)
                || (it.bookStart.isBefore(bookStart) || it.bookEnd.isAfter(bookStart))
                || (it.bookStart.isBefore(bookEnd) || it.bookEnd.isAfter(bookEnd)))
        }
        if (isTimeBooked) {
            throw TimeAlreadyBooked("Time for property with id: $propertyId from: $bookStart till: $bookEnd, cross with another booking")
        }
        val id = UUID.randomUUID().toString()
        val booking = Booking(id, propertyId, tenantId, bookStart, bookEnd, BookingStatus.REQUESTED)
        storage[id] = booking
        return booking
    }

    override suspend fun changeStatus(bookingId: String, newStatus: BookingStatus): Booking {
        if (storage.containsKey(bookingId)) {
            val booking = storage[bookingId]!!
            val updateBooking = booking.updateStatus(newStatus)
            storage[bookingId] = updateBooking
            return updateBooking
        }
        throw BookingNotFoundException("Booking with id: $bookingId not found")
    }

    override suspend fun delete(bookingId: String): Booking {
        if (storage.containsKey(bookingId)) {
            val booking = storage[bookingId]!!
            storage.remove(bookingId)
            return booking
        }
        throw BookingNotFoundException("Booking with id: $bookingId not found")
    }
}
