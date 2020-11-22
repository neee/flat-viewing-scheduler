package flat.viewing.scheduler.domain.booking

import flat.viewing.scheduler.domain.exceptions.BookingApproveException
import flat.viewing.scheduler.domain.exceptions.BookingDeleteException
import flat.viewing.scheduler.domain.exceptions.BookingRejectException
import flat.viewing.scheduler.domain.notifications.BookingNotifier
import flat.viewing.scheduler.domain.properties.PropertiesRepository
import flat.viewing.scheduler.infra.validators.BookingPeriodValidator
import flat.viewing.scheduler.infra.validators.BookingPropertyValidator
import flat.viewing.scheduler.infra.validators.BookingTenantValidator
import flat.viewing.scheduler.infra.validators.BookingValidator
import java.time.Duration
import java.time.Instant
import java.time.temporal.TemporalAmount

class BookingManager(
    private val notifier: BookingNotifier,
    private val bookingRepository: BookingRepository,
    private val propertiesRepository: PropertiesRepository,
    private val bookingPeriodValidator: BookingPeriodValidator,
    private val bookingTenantValidator: BookingTenantValidator,
    private val bookingPropertyValidator: BookingPropertyValidator,
    private val bookingValidator: BookingValidator
) {

    suspend fun book(tenantId: String, propertyId: String, bookStart: Instant, bookInterval: TemporalAmount = DEFAULT_BOOKING_DURATION) {
        val bookEnd = bookStart.plus(bookInterval)

        bookingTenantValidator.validate(tenantId)
        bookingPropertyValidator.validate(propertyId)
        bookingPeriodValidator.validate(bookStart, bookEnd)

        val newBooking = bookingRepository.add(tenantId, propertyId, bookStart, bookEnd)
        notifier.notify(newBooking, BookingStatus.REQUESTED)
    }

    suspend fun approve(currentTenantId: String, bookingId: String) {
        bookingTenantValidator.validate(currentTenantId)
        bookingValidator.validate(bookingId)

        if (propertiesRepository.get(bookingRepository.get(bookingId).propertyId).tenantId != currentTenantId) {
            throw BookingApproveException("Booking with id: $bookingId wasn't approved by user: $currentTenantId")
        }

        val approvedBooking = bookingRepository.changeStatus(bookingId, BookingStatus.APPROVED)
        notifier.notify(approvedBooking, BookingStatus.APPROVED)
    }

    suspend fun reject(currentTenantId: String, bookingId: String) {
        bookingTenantValidator.validate(currentTenantId)
        bookingValidator.validate(bookingId)

        if (propertiesRepository.get(bookingRepository.get(bookingId).propertyId).tenantId != currentTenantId) {
            throw BookingRejectException("Booking with id: $bookingId wasn't rejected by user: $currentTenantId")
        }

        val rejectedBooking = bookingRepository.changeStatus(bookingId, BookingStatus.REJECTED)
        notifier.notify(rejectedBooking, BookingStatus.REJECTED)
    }

    suspend fun delete(newTenantId: String, bookingId: String) {
        bookingTenantValidator.validate(newTenantId)
        bookingValidator.validate(bookingId)

        if (propertiesRepository.get(bookingRepository.get(bookingId).propertyId).tenantId != newTenantId) {
            throw BookingDeleteException("Booking with id: $bookingId wasn't deleted by user: $newTenantId")
        }
        val deletedSchedule = bookingRepository.delete(bookingId)
        notifier.notify(deletedSchedule, BookingStatus.DELETED)
    }

    companion object {
        val DEFAULT_BOOKING_DURATION: Duration = Duration.ofMinutes(20)
    }
}
