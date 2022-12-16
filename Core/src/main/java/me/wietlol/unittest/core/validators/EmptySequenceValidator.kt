package me.wietlol.unittest.core.validators

import me.wietlol.unittest.core.models.TestOptions
import me.wietlol.utils.common.collections.BufferedSequence

class EmptySequenceValidator<T>(
	override val options: TestOptions,
	val testForEmpty: Boolean,
) : Validator<BufferedSequence<T>>, ValidatorHelper
{
	override fun validate(value: BufferedSequence<T>): Validation
	{
		val isValid = testForEmpty == (value.firstOrNull() == null)
		val message = generateMessage(isValid)
		return Validation(isValid, message)
	}
	
	private fun generateMessage(isValid: Boolean): String
	{
		val function = if (testForEmpty) "isEmpty" else "isNotEmpty"
		return if (isValid)
			"$messageIndent'$function()' assertion succeeded"
		else
			"$messageIndent'$function()' assertion failed:\n" +
				"${subMessageIndent}type: sequence"
	}
}
