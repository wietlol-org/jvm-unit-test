package me.wietlol.unittest.core.validators

import me.wietlol.unittest.core.models.TestOptions

class EmptyListValidator<T>(
	override val options: TestOptions,
	val testForEmpty: Boolean,
) : Validator<Collection<T>>, ValidatorHelper
{
	override fun validate(value: Collection<T>): Validation
	{
		val isValid = testForEmpty == value.isEmpty()
		val message = generateMessage(value, isValid)
		return Validation(isValid, message)
	}
	
	private fun generateMessage(value: Collection<T>, isValid: Boolean): String
	{
		val function = if (testForEmpty) "isEmpty" else "isNotEmpty"
		return if (isValid)
			"$messageIndent'$function()' assertion succeeded"
		else
			"$messageIndent'$function()' assertion failed:\n" +
				"${subMessageIndent}size: ${value.size}"
	}
}
