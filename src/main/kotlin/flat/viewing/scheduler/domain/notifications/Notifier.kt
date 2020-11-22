package flat.viewing.scheduler.domain.notifications

import flat.viewing.scheduler.domain.users.User

interface Notifier {

    suspend fun notify(user: User, message: String)
}
