package flat.viewing.scheduler.infra.repo

import TestData.Companion.PROPERTY_ADDRESS_1
import TestData.Companion.PROPERTY_ADDRESS_2
import TestData.Companion.PROPERTY_ID_1
import TestData.Companion.PROPERTY_ID_2
import TestData.Companion.TENANT_ID_1
import TestData.Companion.TENANT_ID_2
import flat.viewing.scheduler.domain.exceptions.PropertyAlreadyExistsException
import flat.viewing.scheduler.domain.exceptions.PropertyNotFoundException
import flat.viewing.scheduler.domain.properties.Property
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

internal class InMemoryPropertiesRepositoryTest {

    @Test
    fun `it receives exception if id not found`(): Unit = runBlocking {
        val repo = InMemoryPropertiesRepository()
        assertThrows(PropertyNotFoundException::class.java) { runBlocking { repo.get(PROPERTY_ID_1) } }
    }

    @Test
    fun `it receives exception if property already added`(): Unit = runBlocking {
        val repo = InMemoryPropertiesRepository()
        val property = Property(PROPERTY_ID_1, TENANT_ID_1, PROPERTY_ADDRESS_1)

        repo.add(property)
        assertThrows(PropertyAlreadyExistsException::class.java) { runBlocking { repo.add(property) } }
    }

    @Test
    fun `it gets all properties`(): Unit = runBlocking {
        val repo = InMemoryPropertiesRepository()
        val property1 = Property(PROPERTY_ID_1, TENANT_ID_1, PROPERTY_ADDRESS_1)
        val property2 = Property(PROPERTY_ID_2, TENANT_ID_2, PROPERTY_ADDRESS_2)

        repo.add(property1)
        repo.add(property2)
        val properties = repo.getAll()

        assertTrue(properties.size == 2)
        assertTrue(properties.contains(property1))
        assertTrue(properties.contains(property2))
    }
}
