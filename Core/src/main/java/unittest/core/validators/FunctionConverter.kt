package unittest.core.validators

import unittest.core.models.TestOptions
import kotlin.reflect.KFunction

class FunctionConverter<T, R>(
	override val options: TestOptions,
	val mapping: T.() -> KFunction<R>,
) : Validator<T>, ValidatorHelper
{
	override fun validate(value: T): Validation
	{
		val function = value.mapping()
		val message = generateMessage(function)
		return Validation(true, message)
	}
	
	fun convert(value: T): R =
		value.mapping().call()
	
	private fun generateMessage(function: KFunction<R>): String =
		"$messageIndent'function { ::${function.name} }' conversion succeeded"
}
