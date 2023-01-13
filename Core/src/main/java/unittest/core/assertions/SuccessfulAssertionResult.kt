package unittest.core.assertions

import unittest.core.models.TestCase
import unittest.core.validators.Validation
import unittest.core.validators.Validator

data class SuccessfulAssertionResult<T>(
	val value: T,
	val assertion: Assertion<*>,
	override val result: Validation,
	val case: TestCase,
) : AssertionResult<T>
{
	override fun <R> validate(validator: Validator<T>, mapper: (T) -> R): AssertionResult<R>
	{
		val result = validator.validate(value)
		return if (result.isValid)
			SuccessfulAssertionResult(mapper(value), this, result, case)
		else
			FailedAssertionResult(value, this, result)
	}
	
	override fun <R> take(mapper: (T) -> R): AssertionResult<R> =
		SuccessfulAssertionResult(mapper(value), this, result, case)
	
	override fun <R> map(mapper: (T) -> R): AssertionResult<R>
	{
		val assertion = case.assertThat(mapper(value), "mapped")
		return SuccessfulAssertionResult(mapper(value), assertion, result, case)
	}
}
