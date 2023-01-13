package unittest.core.validators

import unittest.core.models.TestOptions
import utils.common.append

class StringRegexValidator(
	override val options: TestOptions,
	val regex: Regex,
	val exactMatch: Boolean,
	val displayLength: Int = options.displayLength,
) : Validator<CharSequence>, ValidatorHelper
{
	override fun validate(value: CharSequence): Validation
	{
		val isValid = if (exactMatch) regex.matches(value) else regex.containsMatchIn(value)
		val message = generateMessage(isValid, value)
		return Validation(isValid, message)
	}
	
	private fun generateMessage(isValid: Boolean, value: CharSequence): String =
		if (isValid)
			"$messageIndent'matches(regex=Regex(${regex.pattern.toJson()}), exactMatch=$exactMatch)' assertion succeeded"
		else
			"$messageIndent'matches(regex=Regex(${regex.pattern.toJson()}), exactMatch=$exactMatch)' assertion failed:\n" +
				"${subMessageIndent}value: ${value.toRawJsonString().toDisplayString()}\n" +
				"${subMessageIndent}pattern: ${regex.pattern}\n" +
				"${subMessageIndent}exactMatch: $exactMatch"
	
	private fun String.toDisplayString(desiredLength: Int = displayLength): String =
		let { if (it.length > desiredLength) it.substring(0, desiredLength - 3).append("...") else it }
			.asJson()
}
