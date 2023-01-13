package unittest.core.validators

import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.module.SimpleModule
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.paramnames.ParameterNamesModule
import unittest.core.models.TestOptions
import unittest.core.util.ExceptionSerializer
import utils.common.recursiveMapBreathFirst
import utils.common.surroundWith
import java.lang.reflect.ParameterizedType

interface ValidatorHelper
{
	val messageIndent: String
		@JsonIgnore
		get() = "\t"
	val subMessageIndent: String
		@JsonIgnore
		get() = "\t\t"
	
	val options: TestOptions
	
	companion object
	{
		// todo extract this out in order to allow users to add more modules
		val objectMapper = ObjectMapper()
			.apply {
				registerModule(SimpleModule().apply {
					addSerializer(Throwable::class.java, ExceptionSerializer())
				})
				registerModule(ParameterNamesModule())
				registerModule(Jdk8Module())
				registerModule(JavaTimeModule())
			}
	}
	
	fun Any?.toDisplayString(desiredLength: Int = options.displayLength): String =
		displayStringOf(this, desiredLength)
	
	fun displayStringOf(obj: Any?, desiredLength: Int = options.displayLength): String
	{
		if (obj is Iterable<*> && obj !is Collection<*>)
			return iterableDisplayStringOf(obj)
		if (obj is Sequence<*>)
			return sequenceDisplayStringOf(obj)
		
		return obj.toJson()
	}
	
	//	fun displayString(obj: Any, desiredLength: Int = options.displayLength): String =
	//		let { if (it.length > desiredLength) it.substring(0, desiredLength - 3).append("...") else it }
	//			.asJson()
	
	fun iterableDisplayStringOf(iterable: Iterable<*>): String
	{
		fun Class<*>.supers(): Sequence<ParameterizedType> =
			genericInterfaces
				.asSequence()
				.filterIsInstance<ParameterizedType>()
				.filter { it.rawType is Class<*> }
		
		fun Class<*>.allSupers(): Sequence<ParameterizedType> =
			supers()
				.recursiveMapBreathFirst { (it.rawType as Class<*>).supers() }
		
		val candidate = iterable.javaClass
			.allSupers()
			.filter { it.rawType == Iterable::class.java }
			.firstOrNull()
			?.actualTypeArguments
			?.first()
		
		if (candidate is Class<*>)
			return "Iterable<${candidate.simpleName}> { ... }"
		
		if (candidate != null)
			return "Iterable<${candidate.typeName}> { ... }"
		
		return "Iterable<*> { ... }"
	}
	
	fun sequenceDisplayStringOf(iterable: Sequence<*>): String
	{
		fun Class<*>.supers(): Sequence<ParameterizedType> =
			genericInterfaces
				.asSequence()
				.filterIsInstance<ParameterizedType>()
				.filter { it.rawType is Class<*> }
		
		fun Class<*>.allSupers(): Sequence<ParameterizedType> =
			supers()
				.recursiveMapBreathFirst { (it.rawType as Class<*>).supers() }
		
		val candidate = iterable.javaClass
			.allSupers()
			.filter { it.rawType == Sequence::class.java }
			.firstOrNull()
			?.actualTypeArguments
			?.first()
		
		if (candidate is Class<*>)
			return "Sequence<${candidate.simpleName}> { ... }"
		
		if (candidate != null)
			return "Sequence<${candidate.typeName}> { ... }"
		
		return "Sequence<*> { ... }"
	}
	
	fun Any?.toJson(): String =
		runCatching { objectMapper.writeValueAsString(this) }
			.getOrElse { toString() }
	
	fun CharSequence.toJson() =
		toRawJsonString().asJson()
	
	fun CharSequence.toRawJsonString() =
		toString()
			.replace("\\", "\\\\")
			.replace("\r", "\\r")
			.replace("\n", "\\n")
			.replace("\t", "\\t")
			.replace("\b", "\\b")
			.replace("\"", "\\\"")
	
	// assumes input is correct json string content
	fun String.asJson() =
		surroundWith("\"")
}
