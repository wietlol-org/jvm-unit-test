package unittest.core.validators

import unittest.core.models.TestOptions
import utils.common.append
import utils.common.prepend
import kotlin.math.max
import kotlin.math.min

class StringNotEqualValidator(
	override val options: TestOptions,
	base: CharSequence,
	val ignoreCase: Boolean,
	val maxDisplayLength: Int = options.displayLength,
) : Validator<CharSequence>, ValidatorHelper
{
	private val baseText = base.toRawJsonString()
	
	override fun validate(value: CharSequence): Validation
	{
		val valueText = value.toRawJsonString()
		val isValid = valueText.equals(baseText, ignoreCase).not()
		val displayLength = min(maxDisplayLength, max(valueText.length, baseText.length))
		val message = generateMessage(valueText, isValid, displayLength)
		return Validation(isValid, message)
	}
	
	private fun generateMessage(valueText: String, isValid: Boolean, displayLength: Int): String =
		if (isValid)
			"$messageIndent'isNotEqualTo(base=${baseText.toDisplayString()}, ignoreCase=$ignoreCase)' assertion succeeded"
		else
		{
			val invalidIndex = findInvalidIndex(valueText, baseText, ignoreCase)
			
			val halfDisplayLength = displayLength / 2
			val offset = if (invalidIndex > halfDisplayLength)
				min(invalidIndex - halfDisplayLength, valueText.length - displayLength)
			else
				0
			
			val valueDisplay = valueText.toDisplayString(displayLength, offset)
			val tailDisplay = baseText.toDisplayString(displayLength, offset)
			
			"$messageIndent'isNotEqualTo(base=${baseText.toDisplayString()}, ignoreCase=$ignoreCase)' assertion failed:\n" +
				"${subMessageIndent}value: $valueDisplay\n" +
				"${subMessageIndent}base:  $tailDisplay\n" +
				"${subMessageIndent}error:  ${" ".repeat(invalidIndex - offset)}^"
		}
	
	private fun String.toDisplayString(displayLength: Int = min(maxDisplayLength, length), offset: Int = 0): String =
		if (offset > 0)
			when
			{
				length - offset <= displayLength -> substring(offset + 3).prepend("...").asJson()
				length - offset > displayLength -> substring(offset + 3).prepend("...").substring(0, displayLength - 3)
					.append("...").asJson()
				else -> TODO("impossible?")
			}
		else
			when
			{
				length <= displayLength -> asJson()
				length > displayLength -> substring(0, displayLength - 3).append("...").asJson()
				else -> TODO("impossible?")
			}
	
	private fun findInvalidIndex(value: String, head: String, ignoreCase: Boolean): Int =
		(0..min(value.length, head.length))
			.asSequence()
			.filter { it == head.length || value[it].equals(head[it], ignoreCase).not() }
			.first()
}
