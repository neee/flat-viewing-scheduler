package flat.viewing.scheduler.infra.validators

import flat.viewing.scheduler.domain.users.UsersRepository

class BookingTenantValidator(private val usersRepository: UsersRepository) {

    suspend fun validate(userId: String) {
        usersRepository.get(userId)
    }
}
