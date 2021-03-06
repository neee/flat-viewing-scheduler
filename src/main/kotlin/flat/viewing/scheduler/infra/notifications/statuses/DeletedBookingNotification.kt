package flat.viewing.scheduler.infra.notifications.statuses

import flat.viewing.scheduler.domain.booking.Booking
import flat.viewing.scheduler.domain.notifications.Notifier
import flat.viewing.scheduler.domain.properties.PropertiesRepository
import flat.viewing.scheduler.domain.users.UsersRepository

class DeletedBookingNotification(
    private val notifier: Notifier,
    private val usersRepository: UsersRepository,
    private val propertiesRepository: PropertiesRepository
) {

    suspend fun notify(booking: Booking) {
        val property = propertiesRepository.get(booking.propertyId)
        val currentTenant = usersRepository.get(property.tenantId)
        val newTenant = usersRepository.get(booking.newTenantId)
        notifier.notify(
            currentTenant,
            "DELETED: The booked earlier time viewing ${booking.bookStart} the property with address: ${property.address} was deleted and now available."
        )
        notifier.notify(
            newTenant,
            "DELETED: The time ${booking.bookStart} your booked earlier viewing the property with address: ${property.address} was deleted. Hope see you again."
        )
    }
}