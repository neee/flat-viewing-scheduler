package flat.viewing.scheduler.infra.notifications

import flat.viewing.scheduler.domain.users.User
import flat.viewing.scheduler.domain.notifications.Notifier

class EmailNotifier: Notifier {

    override suspend fun notify(user: User, message: String) {
        println("""
            email send to: ${user.email}
                message: $message
        """.trimIndent())
    }
}
