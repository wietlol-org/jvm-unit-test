package unittest.core.validators

import unittest.core.models.TestOptions

class GenericValidator<T>(
	override val options: TestOptions,
	val name: String,
	val assert: (T) -> Boolean,
) : Validator<T>, ValidatorHelper
{
	override fun validate(value: T): Validation
	{
		val isValid = assert(value)
		val message = generateMessage(value, isValid)
		return Validation(isValid, message)
	}
	
	private fun generateMessage(value: T, isValid: Boolean): String =
		if (isValid)
			"$messageIndent'$name' assertion succeeded"
		else
			"$messageIndent'$name' assertion failed:\n" +
				"${subMessageIndent}value: $value"
}
