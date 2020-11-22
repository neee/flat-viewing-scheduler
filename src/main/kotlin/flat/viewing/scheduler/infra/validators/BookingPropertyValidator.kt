package flat.viewing.scheduler.infra.validators

import flat.viewing.scheduler.domain.properties.PropertiesRepository

class BookingPropertyValidator(private val propertiesRepository: PropertiesRepository) {

    suspend fun validate(propertyId: String) {
        propertiesRepository.get(propertyId)
    }
}
