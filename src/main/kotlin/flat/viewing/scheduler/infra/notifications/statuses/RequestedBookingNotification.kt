package flat.viewing.scheduler.infra.notifications.statuses

import flat.viewing.scheduler.domain.booking.Booking
import flat.viewing.scheduler.domain.notifications.Notifier
import flat.viewing.scheduler.domain.properties.PropertiesRepository
import flat.viewing.scheduler.domain.users.UsersRepository

class RequestedBookingNotification(
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
            "REQUESTED: Someone booked the property with address: ${property.address} viewing at ${booking.bookStart}"
        )
        notifier.notify(
            newTenant,
            "REQUESTED: You have just booked the property with address: ${property.address} viewing at ${booking.bookStart} wait for approve please"
        )
    }
}