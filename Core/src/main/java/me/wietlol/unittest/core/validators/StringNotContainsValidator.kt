package me.wietlol.unittest.core.validators

import me.wietlol.unittest.core.models.TestOptions
import me.wietlol.utils.common.append

class StringNotContainsValidator(
	override val options: TestOptions,
	search: CharSequence,
	val ignoreCase: Boolean,
	val displayLength: Int = options.displayLength,
) : Validator<CharSequence>, ValidatorHelper
{
	private val searchText = search.toRawJsonString()
	
	override fun validate(value: CharSequence): Validation
	{
		val valueText = value.toRawJsonString()
		val isValid = !valueText.contains(searchText, ignoreCase)
		val message = generateMessage(isValid, valueText)
		return Validation(isValid, message)
	}
	
	private fun generateMessage(isValid: Boolean, valueText: String): String =
		if (isValid)
			"$messageIndent'notContains(search=${searchText.toDisplayString()}, ignoreCase=$ignoreCase)' assertion succeeded"
		else
			"$messageIndent'notContains(search=${searchText.toDisplayString()}, ignoreCase=$ignoreCase)' assertion failed:\n" +
				"${subMessageIndent}value:  ${valueText.toDisplayString()}\n" +
				"${subMessageIndent}search: ${searchText.toDisplayString()}"
	
	private fun String.toDisplayString(desiredLength: Int = displayLength): String =
		let { if (it.length > desiredLength) it.substring(0, desiredLength - 3).append("...") else it }
			.asJson()
}
