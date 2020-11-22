package flat.viewing.scheduler.infra.notifications

import flat.viewing.scheduler.domain.booking.BookingStatus
import flat.viewing.scheduler.domain.booking.Booking
import flat.viewing.scheduler.domain.notifications.BookingNotifier
import flat.viewing.scheduler.domain.notifications.Notifier
import flat.viewing.scheduler.domain.properties.PropertiesRepository
import flat.viewing.scheduler.domain.users.UsersRepository
import flat.viewing.scheduler.infra.notifications.statuses.ApprovedBookingNotification
import flat.viewing.scheduler.infra.notifications.statuses.DeletedBookingNotification
import flat.viewing.scheduler.infra.notifications.statuses.RejectedBookingNotification
import flat.viewing.scheduler.infra.notifications.statuses.RequestedBookingNotification

class BookingNotificationResolver(
    notifier: Notifier,
    usersRepository: UsersRepository,
    propertiesRepository: PropertiesRepository
) : BookingNotifier {

    private val requestedBookingNotification = RequestedBookingNotification(notifier, usersRepository, propertiesRepository)
    private val approvedBookingNotification = ApprovedBookingNotification(notifier, usersRepository, propertiesRepository)
    private val rejectedBookingNotification = RejectedBookingNotification(notifier, usersRepository, propertiesRepository)
    private val deletedBookingNotification = DeletedBookingNotification(notifier, usersRepository, propertiesRepository)

    override suspend fun notify(booking: Booking, requested: BookingStatus) {
        when (requested) {
            BookingStatus.REQUESTED -> requestedBookingNotification.notify(booking)
            BookingStatus.APPROVED -> approvedBookingNotification.notify(booking)
            BookingStatus.REJECTED -> rejectedBookingNotification.notify(booking)
            BookingStatus.DELETED -> deletedBookingNotification.notify(booking)
        }
    }
}
