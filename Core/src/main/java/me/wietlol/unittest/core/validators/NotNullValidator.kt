package me.wietlol.unittest.core.validators

import me.wietlol.unittest.core.models.TestOptions

class NotNullValidator<in T>(
	override val options: TestOptions,
) : Validator<T>, ValidatorHelper
{
	override fun validate(value: T): Validation
	{
		val isValid = value != null
		
		val message = if (isValid)
			"$messageIndent'isNotNull()' assertion succeeded"
		else
			"$messageIndent'isNotNull()' assertion failed"
		
		return Validation(isValid, message)
	}
}
