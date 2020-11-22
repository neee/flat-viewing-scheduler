package flat.viewing.scheduler.domain.properties

interface PropertiesRepository {

    suspend fun getAll(): Collection<Property>

    suspend fun get(id: String): Property

    suspend fun add(property: Property): Property
}
