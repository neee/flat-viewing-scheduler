package flat.viewing.scheduler.infra.repo

import TestData.Companion.BOOKING_ID_1
import TestData.Companion.PROPERTY_ID_1
import TestData.Companion.PROPERTY_ID_2
import TestData.Companion.TENANT_ID_1
import TestData.Companion.TENANT_ID_2
import flat.viewing.scheduler.domain.booking.BookingStatus.REQUESTED
import flat.viewing.scheduler.domain.booking.Booking
import flat.viewing.scheduler.domain.booking.BookingManager.Companion.DEFAULT_BOOKING_DURATION
import flat.viewing.scheduler.domain.exceptions.BookingNotFoundException
import flat.viewing.scheduler.domain.exceptions.TimeAlreadyBooked
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import java.time.Instant
import java.util.stream.Stream

internal class InMemoryBookingRepositoryTest {

    @Test
    fun `it checks already booked time`(): Unit = runBlocking {
        val repo = InMemoryBookingRepository()
        val bookStart = Instant.now()
        val bookEnd = bookStart.plus(DEFAULT_BOOKING_DURATION)

        repo.add(TENANT_ID_1, PROPERTY_ID_1, bookStart, bookEnd).id
        assertThrows(TimeAlreadyBooked::class.java) { runBlocking { repo.add(TENANT_ID_1, PROPERTY_ID_1, bookStart, bookEnd) } }
    }

    @Test
    fun `it receives exception if id not found`(): Unit = runBlocking {
        val repo = InMemoryBookingRepository()
        assertThrows(BookingNotFoundException::class.java) { runBlocking { repo.get(BOOKING_ID_1) } }
    }

    @Test
    fun `it gets all bookings`() = runBlocking {
        val repo = InMemoryBookingRepository()
        val bookStart1 = Instant.now()
        val bookEnd1 = bookStart1.plus(DEFAULT_BOOKING_DURATION)
        val bookStart2 = Instant.now()
        val bookEnd2 = bookStart2.plus(DEFAULT_BOOKING_DURATION)

        val bookingId1 = repo.add(TENANT_ID_1, PROPERTY_ID_1, bookStart1, bookEnd1).id
        val bookingId2 = repo.add(TENANT_ID_2, PROPERTY_ID_2, bookStart2, bookEnd2).id
        val bookings = repo.getAll()

        assertTrue(bookings.size == 2)
        assertTrue(bookings.contains(Booking(bookingId1, PROPERTY_ID_1, TENANT_ID_1, bookStart1, bookEnd1, REQUESTED)))
        assertTrue(bookings.contains(Booking(bookingId2, PROPERTY_ID_2, TENANT_ID_2, bookStart2, bookEnd2, REQUESTED)))
    }

    @ParameterizedTest
    @MethodSource("testData")
    fun `it checks success add booking`(tenantId: String, propertyId: String, bookStart: Instant) = runBlocking {
        val bookEnd = bookStart.plus(DEFAULT_BOOKING_DURATION)
        val repo = InMemoryBookingRepository()
        val bookingId = repo.add(tenantId, propertyId, bookStart, bookEnd).id
        val insertedBooking = repo.get(bookingId)

        assertEquals(tenantId, insertedBooking.newTenantId)
        assertEquals(propertyId, insertedBooking.propertyId)
        assertEquals(bookStart, insertedBooking.bookStart)
    }

    fun testData(): Stream<Arguments> = Stream.of(
        Arguments.of(TENANT_ID_1, PROPERTY_ID_1, Instant.now()),
        Arguments.of(TENANT_ID_1, PROPERTY_ID_2, Instant.now()),
    )
}
