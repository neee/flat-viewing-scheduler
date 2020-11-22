package flat.viewing.scheduler.infra.serializers

import com.google.gson.JsonElement
import com.google.gson.JsonPrimitive
import com.google.gson.JsonSerializationContext
import com.google.gson.JsonSerializer
import java.lang.reflect.Type
import java.time.Instant


class InstantAdapter : JsonSerializer<Instant> {

    override fun serialize(instant: Instant, typeOfSrc: Type, context: JsonSerializationContext): JsonElement {
        return JsonPrimitive(instant.toString())
    }
}
