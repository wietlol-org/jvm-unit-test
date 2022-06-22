package me.wietlol.unittest.core.validators

import me.wietlol.unittest.core.models.TestOptions
import me.wietlol.utils.common.append
import me.wietlol.utils.common.prepend
import kotlin.math.max
import kotlin.math.min

class StringStartsWithValidator(
	override val options: TestOptions,
	head: CharSequence,
	val ignoreCase: Boolean,
	val maxDisplayLength: Int = 100,
) : Validator<CharSequence>, ValidatorHelper
{
	private val headText = head.toRawJsonString()
	
	override fun validate(value: CharSequence): Validation
	{
		val valueText = value.toRawJsonString()
		val isValid = valueText.startsWith(headText, ignoreCase)
		val displayLength = min(maxDisplayLength, max(valueText.length, headText.length))
		val message = generateMessage(valueText, isValid, displayLength)
		return Validation(isValid, message)
	}
	
	private fun generateMessage(valueText: String, isValid: Boolean, displayLength: Int): String
	{
		return if (isValid)
			"$messageIndent'startsWith(head=${headText.toDisplayString()}, ignoreCase=$ignoreCase)' assertion succeeded"
		else
		{
			val invalidIndex = findInvalidIndex(valueText, headText, ignoreCase)
			
			val halfDisplayLength = displayLength / 2
			val offset = if (invalidIndex > halfDisplayLength)
				min(invalidIndex - halfDisplayLength, valueText.length - displayLength)
			else
				0
			
			val valueDisplay = valueText.toDisplayString(displayLength, offset)
			val tailDisplay = headText.toDisplayString(displayLength, offset)
			
			"$messageIndent'startsWith(head=${headText.toDisplayString()}, ignoreCase=$ignoreCase)' assertion failed:\n" +
				"${subMessageIndent}value: $valueDisplay\n" +
				"${subMessageIndent}head:  $tailDisplay\n" +
				"${subMessageIndent}error:  ${" ".repeat(invalidIndex - offset)}^"
		}
	}
	
	private fun String.toDisplayString(displayLength: Int = min(maxDisplayLength, length), offset: Int = 0): String =
		if (offset > 0)
			when
			{
				length - offset < displayLength -> substring(offset + 3).prepend("...").asJson()
				length - offset == displayLength -> substring(offset + 3).prepend("...").asJson()
				length - offset > displayLength -> substring(offset + 3).prepend("...").substring(0, displayLength - 3)
					.append("...").asJson()
				else -> TODO("impossible?")
			}
		else
			when
			{
				length < displayLength -> asJson()
				length == displayLength -> asJson()
				length > displayLength -> substring(0, displayLength - 3).append("...").asJson()
				else -> TODO("impossible?")
			}
	
	private fun findInvalidIndex(value: String, head: String, ignoreCase: Boolean): Int =
		(0..min(value.length, head.length))
			.asSequence()
			.filter { it == value.length || value[it].equals(head[it], ignoreCase).not() }
			.first()
}
