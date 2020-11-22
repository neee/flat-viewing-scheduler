package flat.viewing.scheduler.infra.repo

import flat.viewing.scheduler.domain.exceptions.UserAlreadyExistsException
import flat.viewing.scheduler.domain.exceptions.UserNotFoundException
import flat.viewing.scheduler.domain.users.User
import flat.viewing.scheduler.domain.users.UsersRepository
import java.util.concurrent.ConcurrentHashMap

class InMemoryUsersRepository : UsersRepository {

    private val storage = ConcurrentHashMap<String, User>()

    override suspend fun getAll(): Collection<User> = storage.values

    override suspend fun get(id: String): User {
        return storage[id] ?: throw UserNotFoundException("User with id: $id not found")
    }

    override suspend fun add(user: User): User {
        if (storage[user.id] == null) {
            storage[user.id] = user
            return user
        }
        throw UserAlreadyExistsException("User with id: ${user.id} already exists")
    }
}