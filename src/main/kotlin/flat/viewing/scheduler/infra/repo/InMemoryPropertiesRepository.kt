package flat.viewing.scheduler.infra.repo

import flat.viewing.scheduler.domain.exceptions.PropertyAlreadyExistsException
import flat.viewing.scheduler.domain.exceptions.PropertyNotFoundException
import flat.viewing.scheduler.domain.properties.PropertiesRepository
import flat.viewing.scheduler.domain.properties.Property
import java.util.concurrent.ConcurrentHashMap

class InMemoryPropertiesRepository : PropertiesRepository {

    private val storage = ConcurrentHashMap<String, Property>()

    override suspend fun getAll(): Collection<Property> = storage.values

    override suspend fun get(id: String): Property {
        return storage[id] ?: throw PropertyNotFoundException("Property with id: $id, not found")
    }

    override suspend fun add(property: Property): Property {
        if (storage[property.id] == null) {
            storage[property.id] = property
            return property
        }
        throw PropertyAlreadyExistsException("User with id: ${property.id} already exists")
    }
}
