package unittest.core.validators

import unittest.core.models.TestOptions

class ConvertToIntegerValidator(
	override val options: TestOptions,
) : Validator<CharSequence>, ValidatorHelper
{
	override fun validate(value: CharSequence): Validation
	{
		val newValue = parse(value)
		val isValid = newValue.isSuccess
		val message = generateMessage(value, isValid, newValue)
		return Validation(isValid, message)
	}
	
	fun parse(value: CharSequence): Result<Int> =
		runCatching { value.toString().toInt() }
	
	private fun generateMessage(value: CharSequence, isValid: Boolean, newValue: Result<Int>): String =
		if (isValid)
			"$messageIndent'toInteger()' assertion succeeded (value: ${newValue.getOrThrow()})"
		else
		{
			val valueDisplay = value.toJson()
			
			"$messageIndent'toInteger()' assertion failed:\n" +
				"${subMessageIndent}value: $valueDisplay\n" +
				"${subMessageIndent}reason: ${newValue.exceptionOrNull()}"
		}
}
