package me.wietlol.unittest.core.assertions

import me.wietlol.unittest.core.validators.Validation

interface AssertionResult<out T> : Assertion<T>
{
	val result: Validation
	
	override fun <R> take(mapper: (T) -> R): AssertionResult<R>
	
	override fun <R> map(mapper: (T) -> R): AssertionResult<R>
}
