package flat.viewing.scheduler.infra.validators

import flat.viewing.scheduler.domain.exceptions.BookingCreationException
import java.time.Clock
import java.time.Duration
import java.time.Instant
import java.time.Period
import java.time.temporal.ChronoUnit
import java.time.temporal.ChronoUnit.DAYS
import java.time.temporal.TemporalAmount

class BookingPeriodValidator(
    private val clock: Clock,
    private val availablePeriod: TemporalAmount = Period.ofDays(DEFAULT_BOOKING_PERIOD_IN_DAYS),
    private val availableStartInterval: TemporalAmount = Duration.ofHours(DEFAULT_AVAILABLE_BOOKING_START_IN_HOURS)
) {

    fun validate(bookStart: Instant, bookEnd: Instant) {
        if (bookStart >= bookEnd) {
            throw BookingCreationException("Booking time are wrong: $bookStart and $bookEnd")
        }
        val availablePeriodStart = clock.instant().plus(availableStartInterval)
        val availablePeriodEnd = availablePeriodStart.truncatedTo(DAYS).plus(availablePeriod)
        if (availablePeriodStart.isAfter(bookStart) || availablePeriodEnd.isBefore(bookEnd)) {
            throw BookingCreationException("Booking time $bookStart and $bookEnd are out of the available range: $availablePeriodStart and $availablePeriodEnd")
        }
        val availableTimeStart = bookStart.truncatedTo(DAYS).plus(DEFAULT_START_VIEWING_HOURS, ChronoUnit.HOURS)
        val availableTimeEnd = bookStart.truncatedTo(DAYS).plus(DEFAULT_END_VIEWING_HOURS, ChronoUnit.HOURS)
        if (bookStart < availableTimeStart || bookEnd > availableTimeEnd) {
            throw BookingCreationException("Booking time are not between: $DEFAULT_START_VIEWING_HOURS and $DEFAULT_END_VIEWING_HOURS hours.")
        }
    }

    companion object {

        private const val DEFAULT_BOOKING_PERIOD_IN_DAYS = 7
        private const val DEFAULT_AVAILABLE_BOOKING_START_IN_HOURS = 24L
        private const val DEFAULT_START_VIEWING_HOURS = 10L
        const val DEFAULT_END_VIEWING_HOURS = 20L
    }
}