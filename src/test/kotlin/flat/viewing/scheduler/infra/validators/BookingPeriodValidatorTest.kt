package flat.viewing.scheduler.infra.validators

import TestData.Companion.MIDDAY_CLOCK_START
import TestData.Companion.MIDNIGHT_CLOCK
import TestData.Companion.MOCK_CLOCK
import flat.viewing.scheduler.domain.booking.BookingManager.Companion.DEFAULT_BOOKING_DURATION
import flat.viewing.scheduler.domain.exceptions.BookingCreationException
import org.junit.jupiter.api.Assertions.assertDoesNotThrow
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.Test
import java.time.temporal.ChronoUnit

internal class BookingPeriodValidatorTest {

    private val bookingPeriodValidator = BookingPeriodValidator(MOCK_CLOCK)

    @Test
    fun `it checks valid dates`() {
        val bookStart = MIDDAY_CLOCK_START
        val bookEnd = bookStart.plus(DEFAULT_BOOKING_DURATION)
        assertDoesNotThrow { bookingPeriodValidator.validate(bookStart, bookEnd) }
    }

    @Test
    fun `it checks out of view days`() {
        val bookStart = MIDNIGHT_CLOCK
        val bookEnd = bookStart.plus(DEFAULT_BOOKING_DURATION)
        assertThrows(BookingCreationException::class.java) { bookingPeriodValidator.validate(bookStart, bookEnd) }
    }

    @Test
    fun `it checks out of start days`() {
        val bookStart = MOCK_CLOCK.instant()
        val bookEnd = bookStart.plus(DEFAULT_BOOKING_DURATION)
        assertThrows(BookingCreationException::class.java) { bookingPeriodValidator.validate(bookStart, bookEnd) }
    }

    @Test
    fun `it checks out of view hours`() {
        val bookStart = MOCK_CLOCK.instant()
            .plus(2, ChronoUnit.DAYS)
            .truncatedTo(ChronoUnit.DAYS)
        val bookEnd = bookStart.plus(DEFAULT_BOOKING_DURATION)
        assertThrows(BookingCreationException::class.java) { bookingPeriodValidator.validate(bookStart, bookEnd) }
    }
}
