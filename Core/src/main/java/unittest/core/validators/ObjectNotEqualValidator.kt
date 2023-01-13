package unittest.core.validators

import unittest.core.models.TestOptions

class ObjectNotEqualValidator<T>(
	override val options: TestOptions,
	val base: T,
	val displayLength: Int = options.displayLength,
) : Validator<T>, ValidatorHelper
{
	override fun validate(value: T): Validation
	{
		val isValid = base != value
		val message = generateMessage(isValid, value)
		return Validation(isValid, message)
	}
	
	private fun generateMessage(isValid: Boolean, value: T): String =
		if (isValid)
			"$messageIndent'isNotEqualTo(base=${base.toJson().toDisplayString()})' assertion succeeded"
		else
			"$messageIndent'isNotEqualTo(base=${base.toJson().toDisplayString()})' assertion failed:\n" +
				"${subMessageIndent}value: ${value.toJson().toDisplayString()}\n" +
				"${subMessageIndent}base:  ${value.toJson().toDisplayString()}"
	
	private fun String.toDisplayString(): String =
		let {
			if (it.length > displayLength + 1)
				when
				{
					it.startsWith("\"") -> it.substring(0, displayLength - 2) + "...\""
					it.startsWith("{") -> it.substring(0, displayLength - 2) + "... }"
					it.startsWith("[") -> it.substring(0, displayLength - 2) + "... ]"
					else -> it
				}
			else
				it
		}
}
