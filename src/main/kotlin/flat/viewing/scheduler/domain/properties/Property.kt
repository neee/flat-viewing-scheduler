package flat.viewing.scheduler.domain.properties

data class Property(
    val id: String,
    val tenantId: String,
    val address: Address
)
