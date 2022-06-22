package me.wietlol.unittest.core.assertions

import me.wietlol.unittest.core.models.TestCase
import me.wietlol.unittest.core.validators.Validator

data class ValueAssertion<T>(
	val value: T,
	val case: TestCase,
) : Assertion<T>
{
	override fun <R> validate(validator: Validator<T>, mapper: (T) -> R): AssertionResult<R>
	{
		val result = validator.validate(value)
		return if (result.isValid)
			SuccessfulAssertionResult(mapper(value), this, result, case)
		else
			FailedAssertionResult(value, this, result)
	}
	
	override fun <R> take(mapper: (T) -> R): Assertion<R> =
		ValueAssertion(mapper(value), case)
	
	override fun <R> map(mapper: (T) -> R): Assertion<R> =
		case.assertThat(mapper(value), "mapped")
	
}
