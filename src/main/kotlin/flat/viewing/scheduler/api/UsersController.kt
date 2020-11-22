package flat.viewing.scheduler.api

import flat.viewing.scheduler.domain.users.User
import flat.viewing.scheduler.domain.users.UsersRepository
import io.ktor.application.call
import io.ktor.http.HttpStatusCode
import io.ktor.request.receive
import io.ktor.response.respond
import io.ktor.routing.Routing
import io.ktor.routing.get
import io.ktor.routing.post

fun Routing.users(usersRepository: UsersRepository) {
    get("/users") {
        call.respond(usersRepository.getAll())
    }
    post("/users") {
        val user = call.receive<User>()
        usersRepository.add(user)
        call.respond(HttpStatusCode.OK)
    }
}
