package unittest.core.validators

import unittest.core.models.TestOptions

class BooleanEqualsValidator(
	override val options: TestOptions,
	val expected: Boolean,
) : Validator<Boolean>, ValidatorHelper
{
	override fun validate(value: Boolean): Validation
	{
		val isValid = value == expected
		val message = generateMessage(value, isValid)
		return Validation(isValid, message)
	}
	
	private fun generateMessage(value: Boolean, isValid: Boolean): String =
		if (isValid)
			"$messageIndent'$operationName()' assertion succeeded"
		else
			"$messageIndent'$operationName()' assertion failed:\n" +
				"${subMessageIndent}value: ${value.toJson()}"
	
	private val operationName: String
		get() =
			if (expected)
				"isTrue"
			else
				"isFalse"
}
