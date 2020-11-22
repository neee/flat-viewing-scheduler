package flat.viewing.scheduler.domain.users

interface UsersRepository {

    suspend fun getAll(): Collection<User>

    suspend fun get(id: String): User

    suspend fun add(user: User): User
}