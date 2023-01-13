package unittest.core.util

import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.databind.SerializerProvider
import com.fasterxml.jackson.databind.ser.std.StdSerializer

class ExceptionSerializer : StdSerializer<Throwable>(null as Class<Throwable>?)
{
	override fun serialize(value: Throwable, gen: JsonGenerator, provider: SerializerProvider) =
		gen.writeString("${value.javaClass.name}: ${value.message ?: "-"}")
}
