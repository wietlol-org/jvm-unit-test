package me.wietlol.unittest.core.validators

import me.wietlol.unittest.core.models.TestOptions
import me.wietlol.utils.common.append

class StringBlankValidator(
	override val options: TestOptions,
	val displayLength: Int = options.displayLength,
) : Validator<CharSequence>, ValidatorHelper
{
	override fun validate(value: CharSequence): Validation
	{
		val isValid = value.isBlank()
		val message = generateMessage(isValid, value)
		return Validation(isValid, message)
	}
	
	private fun generateMessage(isValid: Boolean, value: CharSequence): String =
		if (isValid)
			"$messageIndent'isBlank()' assertion succeeded"
		else
			"$messageIndent'isBlank()' assertion failed:\n" +
				"${subMessageIndent}value: ${value.toRawJsonString().toDisplayString()}"
	
	private fun String.toDisplayString(desiredLength: Int = displayLength): String =
		let { if (it.length > desiredLength) it.substring(0, desiredLength - 3).append("...") else it }
			.asJson()
}
