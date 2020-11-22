import flat.viewing.scheduler.domain.booking.BookingStatus
import flat.viewing.scheduler.domain.booking.Booking
import flat.viewing.scheduler.domain.booking.BookingManager.Companion.DEFAULT_BOOKING_DURATION
import flat.viewing.scheduler.domain.properties.Address
import flat.viewing.scheduler.domain.properties.Property
import flat.viewing.scheduler.domain.users.User
import java.time.Clock
import java.time.Instant
import java.time.ZoneId
import java.time.temporal.ChronoUnit

class TestData {
    companion object {

        val MOCK_CLOCK: Clock = Clock.fixed(Instant.parse("2020-11-01T11:00:00Z"), ZoneId.of("UTC"))
        val MIDNIGHT_CLOCK: Instant = MOCK_CLOCK.instant().truncatedTo(ChronoUnit.DAYS)
        val MIDDAY_CLOCK_START: Instant = MIDNIGHT_CLOCK.plus(2L, ChronoUnit.DAYS).plus(12L, ChronoUnit.HOURS)
        val MIDDAY_CLOCK_END: Instant = MIDDAY_CLOCK_START.plus(DEFAULT_BOOKING_DURATION)

        const val TENANT_ID_1 = "tenant_1"
        const val TENANT_NAME_1 = "Name_1"
        const val TENANT_SURNAME_1 = "Surname_1"
        const val TENANT_EMAIL_1 = "test1@email.com"
        val TENANT_1 = User(TENANT_ID_1, TENANT_NAME_1, TENANT_SURNAME_1, TENANT_EMAIL_1)

        const val TENANT_ID_2 = "tenant_2"
        const val TENANT_NAME_2 = "Name_2"
        const val TENANT_SURNAME_2 = "Surname_2"
        const val TENANT_EMAIL_2 = "test2@email.com"
        val TENANT_2 = User(TENANT_ID_2, TENANT_NAME_2, TENANT_SURNAME_2, TENANT_EMAIL_2)

        const val PROPERTY_ID_1 = "property_1"
        val PROPERTY_ADDRESS_1 = Address("Moscow", "Vinokurova", "15b2", "1")
        val PROPERTY_1 = Property(PROPERTY_ID_1, TENANT_ID_1, PROPERTY_ADDRESS_1)

        const val PROPERTY_ID_2 = "property_2"
        val PROPERTY_ADDRESS_2 = Address("Moscow", "Lenina", "3", "12")

        const val BOOKING_ID_1 = "booking_1"
        val BOOKING_1 = Booking(BOOKING_ID_1, PROPERTY_ID_1, TENANT_ID_2, MIDDAY_CLOCK_START, MIDDAY_CLOCK_END, BookingStatus.REQUESTED)
    }
}
