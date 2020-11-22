package flat.viewing.scheduler.domain.properties

data class Address(
    val city: String,
    val street: String,
    val building: String,
    val flat: String
) {

    override fun toString(): String {
        return "$city, $street, $building, $flat"
    }
}
