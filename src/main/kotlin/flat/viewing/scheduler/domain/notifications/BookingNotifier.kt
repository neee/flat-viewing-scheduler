package flat.viewing.scheduler.domain.notifications

import flat.viewing.scheduler.domain.booking.BookingStatus
import flat.viewing.scheduler.domain.booking.Booking

interface BookingNotifier {

    suspend fun notify(booking: Booking, requested: BookingStatus)
}
