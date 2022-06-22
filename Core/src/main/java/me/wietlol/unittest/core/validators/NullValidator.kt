package me.wietlol.unittest.core.validators

import me.wietlol.unittest.core.models.TestOptions

class NullValidator<in T>(
	override val options: TestOptions,
) : Validator<T>, ValidatorHelper
{
	override fun validate(value: T): Validation
	{
		val isValid = value == null
		
		val message = if (isValid)
			"$messageIndent'isNull()' assertion succeeded"
		else
			"$messageIndent'isNull()' assertion failed:\n" +
				"${subMessageIndent}value: $value"
		
		return Validation(isValid, message)
	}
}
