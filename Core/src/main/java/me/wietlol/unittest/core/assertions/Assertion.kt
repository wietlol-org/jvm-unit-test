package me.wietlol.unittest.core.assertions

import me.wietlol.unittest.core.validators.Validator

interface Assertion<out T>
{
	fun <R> validate(validator: Validator<T>, mapper: (T) -> R): AssertionResult<R>
	
	fun <R> take(mapper: (T) -> R): Assertion<R>
	
	fun <R> map(mapper: (T) -> R): Assertion<R>
}
