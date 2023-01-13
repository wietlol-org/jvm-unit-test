package unittest.core.validators

import unittest.core.models.TestOptions

class ThrowExceptionValidator<E : Throwable>(
	override val options: TestOptions,
	val expectedClass: Class<E>,
) : Validator<Throwable?>, ValidatorHelper
{
	override fun validate(value: Throwable?): Validation
	{
		val isValid = value != null && expectedClass.isAssignableFrom(value.javaClass)
		return Validation(isValid, generateMessage(isValid, value?.javaClass, expectedClass))
	}
	
	private fun generateMessage(isValid: Boolean, valueType: Class<Throwable>?, expectedType: Class<E>): String =
		when
		{
			isValid -> "$messageIndent'assertThrows()' assertion succeeded"
			valueType == null -> "$messageIndent'assertThrows()' assertion failed:\n" +
				"${subMessageIndent}message: the function did not throw an exception"
			else -> "$messageIndent'assertThrows()' assertion failed:\n" +
				"${subMessageIndent}message: the exception was of an incorrect type\n" +
				"${subMessageIndent}valueType:  ${valueType.name}\n" +
				"${subMessageIndent}expectedType: ${expectedType.name}"
		}
	
	companion object
	{
		inline operator fun <reified E : Throwable> invoke(options: TestOptions) =
			ThrowExceptionValidator(options, E::class.java)
	}
}
