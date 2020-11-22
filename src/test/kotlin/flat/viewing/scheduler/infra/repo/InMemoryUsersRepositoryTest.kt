package flat.viewing.scheduler.infra.repo

import TestData.Companion.TENANT_EMAIL_1
import TestData.Companion.TENANT_EMAIL_2
import TestData.Companion.TENANT_ID_1
import TestData.Companion.TENANT_ID_2
import TestData.Companion.TENANT_NAME_1
import TestData.Companion.TENANT_NAME_2
import TestData.Companion.TENANT_SURNAME_1
import TestData.Companion.TENANT_SURNAME_2
import flat.viewing.scheduler.domain.exceptions.UserAlreadyExistsException
import flat.viewing.scheduler.domain.exceptions.UserNotFoundException
import flat.viewing.scheduler.domain.users.User
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

internal class InMemoryUsersRepositoryTest {

    @Test
    fun `it receives exception if id not found`(): Unit = runBlocking {
        val repo = InMemoryUsersRepository()
        assertThrows(UserNotFoundException::class.java) { runBlocking { repo.get(TENANT_ID_1) } }
    }

    @Test
    fun `it receives exception if tenant already added`(): Unit = runBlocking {
        val repo = InMemoryUsersRepository()
        val user = User(TENANT_ID_1, TENANT_NAME_1, TENANT_SURNAME_1, TENANT_EMAIL_1)

        repo.add(user)
        assertThrows(UserAlreadyExistsException::class.java) { runBlocking { repo.add(user) } }
    }

    @Test
    fun `it gets all users`(): Unit = runBlocking {
        val repo = InMemoryUsersRepository()
        val user1 = User(TENANT_ID_1, TENANT_NAME_1, TENANT_SURNAME_1, TENANT_EMAIL_1)
        val user2 = User(TENANT_ID_2, TENANT_NAME_2, TENANT_SURNAME_2, TENANT_EMAIL_2)

        repo.add(user1)
        repo.add(user2)
        val properties = repo.getAll()

        assertTrue(properties.size == 2)
        assertTrue(properties.contains(user1))
        assertTrue(properties.contains(user2))
    }
}
