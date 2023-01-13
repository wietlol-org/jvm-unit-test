package unittest.core.validators

import unittest.core.models.TestOptions
import utils.common.collections.BufferedSequence

class ContainsSequenceValidator<T>(
	override val options: TestOptions,
	val needle: T,
) : Validator<BufferedSequence<T>>, ValidatorHelper
{
	override fun validate(value: BufferedSequence<T>): Validation
	{
		val isValid = value.contains(needle)
		val message = generateMessage(isValid, needle)
		return Validation(isValid, message)
	}
	
	private fun generateMessage(isValid: Boolean, needle: T): String =
		if (isValid)
			"$messageIndent'contains(needle=${needle.toDisplayString()})' assertion succeeded"
		else
			"$messageIndent'contains(needle=${needle.toDisplayString()})' assertion failed:\n" +
				"${subMessageIndent}type:   sequence\n" +
				"${subMessageIndent}needle: ${needle.toDisplayString()}"
}
