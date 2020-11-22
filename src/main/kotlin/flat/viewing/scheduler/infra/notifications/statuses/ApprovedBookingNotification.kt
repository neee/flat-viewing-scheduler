package flat.viewing.scheduler.infra.notifications.statuses

import flat.viewing.scheduler.domain.booking.Booking
import flat.viewing.scheduler.domain.notifications.Notifier
import flat.viewing.scheduler.domain.properties.PropertiesRepository
import flat.viewing.scheduler.domain.users.UsersRepository

class ApprovedBookingNotification(
    private val notifier: Notifier,
    private val usersRepository: UsersRepository,
    private val propertiesRepository: PropertiesRepository
) {

    suspend fun notify(booking: Booking) {
        val property = propertiesRepository.get(booking.propertyId)
        val newTenant = usersRepository.get(booking.newTenantId)
        notifier.notify(
            newTenant,
            "APPROVE: The time your booked earlier viewing the property with address: ${property.address} approved! See you at  at ${booking.bookStart}!"
        )
    }
}