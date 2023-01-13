package unittest.core.validators

import unittest.core.models.TestOptions
import utils.common.append
import utils.common.prepend
import kotlin.math.max
import kotlin.math.min

class StringEndsWithValidator(
	override val options: TestOptions,
	tail: CharSequence,
	val ignoreCase: Boolean,
	val maxDisplayLength: Int = 100,
) : Validator<CharSequence>, ValidatorHelper
{
	private val tailText = tail.toRawJsonString()
	
	override fun validate(value: CharSequence): Validation
	{
		val valueText = value.toRawJsonString()
		val isValid = valueText.endsWith(tailText, ignoreCase)
		val displayLength = min(maxDisplayLength, max(valueText.length, tailText.length))
		val message = generateMessage(valueText, isValid, displayLength)
		return Validation(isValid, message)
	}
	
	private fun generateMessage(valueText: String, isValid: Boolean, displayLength: Int): String
	{
		return if (isValid)
			"$messageIndent'endsWith(tail=${tailText.toDisplayString()}, ignoreCase=$ignoreCase)' assertion succeeded"
		else
		{
			val invalidIndex = findLastInvalidIndex(valueText, tailText, ignoreCase) // from right
			
			val halfDisplayLength = displayLength / 2
			val offset =
				min(max(valueText.length, tailText.length), max(displayLength, invalidIndex + halfDisplayLength))
			
			val valueDisplay = valueText.toDisplayString(
				offset,
				displayLength,
				min(max(0, tailText.length - valueText.length), displayLength - valueText.length)
			)
			val tailDisplay = tailText.toDisplayString(
				offset,
				displayLength,
				min(max(0, valueText.length - tailText.length), displayLength - tailText.length)
			)
			
			"$messageIndent'endsWith(tail=${tailText.toDisplayString()}, ignoreCase=$ignoreCase)' assertion failed:\n" +
				"${subMessageIndent}value: $valueDisplay\n" +
				"${subMessageIndent}tail:  $tailDisplay\n" +
				"${subMessageIndent}error: ${" ".repeat(offset - invalidIndex)}^"
		}
	}
	
	private fun String.toDisplayString(desiredLength: Int = min(maxDisplayLength, length)): String =
		toDisplayString(desiredLength, desiredLength, 0)
	
	private fun String.toDisplayString(fromRightIndex: Int, desiredLength: Int, indent: Int): String =
		let { if (fromRightIndex + 1 < it.length) it.substring(it.length - fromRightIndex + 3).prepend("...") else it }
			.let { if (it.length > desiredLength) it.substring(0, desiredLength - 3).append("...") else it }
			.asJson()
			.let { if (indent > 0) it.prepend(" ".repeat(indent)) else it }
	
	private fun findLastInvalidIndex(value: String, tail: String, ignoreCase: Boolean): Int =
		findInvalidIndex(value.reversed(), tail.reversed(), ignoreCase)
	
	private fun findInvalidIndex(value: String, head: String, ignoreCase: Boolean): Int =
		(0..min(value.length, head.length))
			.asSequence()
			.filter { it == value.length || value[it].equals(head[it], ignoreCase).not() }
			.first()
}
