package flat.viewing.scheduler.domain.booking

import TestData.Companion.BOOKING_1
import TestData.Companion.BOOKING_ID_1
import TestData.Companion.MIDDAY_CLOCK_END
import TestData.Companion.MIDDAY_CLOCK_START
import TestData.Companion.MOCK_CLOCK
import TestData.Companion.PROPERTY_1
import TestData.Companion.PROPERTY_ID_1
import TestData.Companion.TENANT_1
import TestData.Companion.TENANT_ID_1
import flat.viewing.scheduler.domain.notifications.BookingNotifier
import flat.viewing.scheduler.domain.properties.PropertiesRepository
import flat.viewing.scheduler.domain.users.UsersRepository
import flat.viewing.scheduler.infra.validators.BookingPeriodValidator
import flat.viewing.scheduler.infra.validators.BookingPropertyValidator
import flat.viewing.scheduler.infra.validators.BookingTenantValidator
import flat.viewing.scheduler.infra.validators.BookingValidator
import io.mockk.coEvery
import io.mockk.mockk
import io.mockk.slot
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertDoesNotThrow

internal class BookingManagerTest {

    private val bookingNotifier = mockk<BookingNotifier>()
    private val bookingRepository = mockk<BookingRepository>()
    private val propertiesRepository = mockk<PropertiesRepository>()
    private val usersRepository = mockk<UsersRepository>()
    private val bookingManager = BookingManager(
        bookingNotifier,
        bookingRepository,
        propertiesRepository,
        BookingPeriodValidator(MOCK_CLOCK),
        BookingTenantValidator(usersRepository),
        BookingPropertyValidator(propertiesRepository),
        BookingValidator(bookingRepository)
    )

    @BeforeEach
    fun init() {
        coEvery { usersRepository.get(TENANT_ID_1) } returns TENANT_1
        coEvery { propertiesRepository.get(PROPERTY_ID_1) } returns PROPERTY_1
        coEvery { bookingRepository.add(TENANT_ID_1, PROPERTY_ID_1, MIDDAY_CLOCK_START, MIDDAY_CLOCK_END) } returns BOOKING_1
        coEvery { bookingRepository.get(BOOKING_ID_1) } returns BOOKING_1
        coEvery { bookingRepository.delete(BOOKING_ID_1) } returns BOOKING_1
    }

    @Test
    fun `it success create booking and call notifier with request status`(): Unit = runBlocking {
        val bookingSlot = slot<Booking>()
        val bookingStatusSlot = slot<BookingStatus>()
        coEvery { bookingNotifier.notify(capture(bookingSlot), capture(bookingStatusSlot)) } returns Unit

        assertDoesNotThrow { runBlocking { bookingManager.book(TENANT_ID_1, PROPERTY_ID_1, MIDDAY_CLOCK_START) } }
        assertEquals(BOOKING_1, bookingSlot.captured)
        assertEquals(BookingStatus.REQUESTED, bookingStatusSlot.captured)
    }

    @Test
    fun `it success approve booking and call notifier with approve status`(): Unit = runBlocking {
        val bookingIdSlot = slot<String>()
        val bookingStatusSlot1 = slot<BookingStatus>()
        coEvery { bookingRepository.changeStatus(capture(bookingIdSlot), capture(bookingStatusSlot1)) } returns BOOKING_1

        val bookingSlot = slot<Booking>()
        val bookingStatusSlot2 = slot<BookingStatus>()
        coEvery { bookingNotifier.notify(capture(bookingSlot), capture(bookingStatusSlot2)) } returns Unit

        assertDoesNotThrow { runBlocking { bookingManager.approve(TENANT_ID_1, BOOKING_ID_1) } }
        assertEquals(BOOKING_ID_1, bookingIdSlot.captured)
        assertEquals(BOOKING_1, bookingSlot.captured)
        assertEquals(BookingStatus.APPROVED, bookingStatusSlot1.captured)
        assertEquals(BookingStatus.APPROVED, bookingStatusSlot2.captured)
    }

    @Test
    fun `it success reject booking and call notifier with reject status`(): Unit = runBlocking {
        val bookingIdSlot = slot<String>()
        val bookingStatusSlot1 = slot<BookingStatus>()
        coEvery { bookingRepository.changeStatus(capture(bookingIdSlot), capture(bookingStatusSlot1)) } returns BOOKING_1

        val bookingSlot = slot<Booking>()
        val bookingStatusSlot2 = slot<BookingStatus>()
        coEvery { bookingNotifier.notify(capture(bookingSlot), capture(bookingStatusSlot2)) } returns Unit

        assertDoesNotThrow { runBlocking { bookingManager.reject(TENANT_ID_1, BOOKING_ID_1) } }
        assertEquals(BOOKING_ID_1, bookingIdSlot.captured)
        assertEquals(BOOKING_1, bookingSlot.captured)
        assertEquals(BookingStatus.REJECTED, bookingStatusSlot1.captured)
        assertEquals(BookingStatus.REJECTED, bookingStatusSlot2.captured)
    }

    @Test
    fun `it success delete booking and call notifier with delete status`(): Unit = runBlocking {
        val bookingSlot = slot<Booking>()
        val bookingStatusSlot2 = slot<BookingStatus>()
        coEvery { bookingNotifier.notify(capture(bookingSlot), capture(bookingStatusSlot2)) } returns Unit

        assertDoesNotThrow { runBlocking { bookingManager.delete(TENANT_ID_1, BOOKING_ID_1) } }
        assertEquals(BOOKING_1, bookingSlot.captured)
        assertEquals(BookingStatus.DELETED, bookingStatusSlot2.captured)
    }
}
