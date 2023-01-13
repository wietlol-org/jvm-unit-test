package unittest.core.validators

import unittest.core.models.TestOptions

class IsInstanceOfValidator<R>(
	override val options: TestOptions,
	val expectedType: Class<R>,
) : Validator<Any>, ValidatorHelper
{
	override fun validate(value: Any): Validation
	{
		val parsed = cast(value)
		val isValid = parsed.isSuccess
		val message = generateMessage(value, isValid)
		return Validation(isValid, message)
	}
	
	fun cast(value: Any): Result<R> =
		runCatching { expectedType.cast(value) }
	
	private fun generateMessage(value: Any, isValid: Boolean): String =
		if (isValid)
			"$messageIndent'isInstanceOf<${expectedType.simpleName}>()' assertion succeeded"
		else
			"$messageIndent'isInstanceOf<${expectedType.simpleName}>()' assertion failed:\n" +
				"${subMessageIndent}value type: ${value.javaClass.name}\n" +
				"${subMessageIndent}expected type: ${expectedType.name}"
}
