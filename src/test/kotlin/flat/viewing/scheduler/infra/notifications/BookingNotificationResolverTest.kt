package flat.viewing.scheduler.infra.notifications

import TestData.Companion.BOOKING_1
import TestData.Companion.PROPERTY_1
import TestData.Companion.PROPERTY_ID_1
import TestData.Companion.TENANT_1
import TestData.Companion.TENANT_2
import TestData.Companion.TENANT_ID_1
import TestData.Companion.TENANT_ID_2
import flat.viewing.scheduler.domain.booking.BookingStatus
import flat.viewing.scheduler.domain.notifications.Notifier
import flat.viewing.scheduler.domain.properties.PropertiesRepository
import flat.viewing.scheduler.domain.users.UsersRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import java.util.stream.Stream

internal class BookingNotificationResolverTest {

    private val usersRepository = mockk<UsersRepository>()
    private val propertiesRepository = mockk<PropertiesRepository>()

    @BeforeEach
    fun init() {
        coEvery { usersRepository.get(TENANT_ID_1) } returns TENANT_1
        coEvery { usersRepository.get(TENANT_ID_2) } returns TENANT_2
        coEvery { propertiesRepository.get(PROPERTY_ID_1) } returns PROPERTY_1
    }

    @MethodSource("testData")
    @ParameterizedTest
    fun `success tenants notifications`(bookingStatus: BookingStatus, expectedCallTimes: Int) = runBlocking {
        val notifier = mockk<Notifier>()
        val notificationResolver = BookingNotificationResolver(notifier, usersRepository, propertiesRepository)
        coEvery { notifier.notify(any(), any()) } returns Unit

        notificationResolver.notify(BOOKING_1, bookingStatus)

        coVerify(exactly = expectedCallTimes) { notifier.notify(any(), any()) }
    }

    fun testData(): Stream<Arguments> = Stream.of(
        Arguments.of(BookingStatus.REQUESTED, 2),
        Arguments.of(BookingStatus.REJECTED, 1),
        Arguments.of(BookingStatus.APPROVED, 1),
        Arguments.of(BookingStatus.DELETED, 2)
    )
}
