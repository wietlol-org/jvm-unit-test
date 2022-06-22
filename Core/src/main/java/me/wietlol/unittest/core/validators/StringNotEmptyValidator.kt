package me.wietlol.unittest.core.validators

import me.wietlol.unittest.core.models.TestOptions
import me.wietlol.utils.common.append

class StringNotEmptyValidator(
	override val options: TestOptions,
	val displayLength: Int = options.displayLength,
) : Validator<CharSequence>, ValidatorHelper
{
	override fun validate(value: CharSequence): Validation
	{
		val isValid = value.isNotEmpty()
		val message = generateMessage(isValid, value)
		return Validation(isValid, message)
	}
	
	private fun generateMessage(isValid: Boolean, value: CharSequence): String =
		if (isValid)
			"$messageIndent'isNotEmpty()' assertion succeeded"
		else
			"$messageIndent'isNotEmpty()' assertion failed:\n" +
				"${subMessageIndent}value: ${value.toRawJsonString().toDisplayString()}"
	
	private fun String.toDisplayString(desiredLength: Int = displayLength): String =
		let { if (it.length > desiredLength) it.substring(0, desiredLength - 3).append("...") else it }
			.asJson()
}
