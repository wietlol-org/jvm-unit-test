package me.wietlol.unittest.core.assertions

import me.wietlol.unittest.core.validators.Validation
import me.wietlol.unittest.core.validators.Validator
import java.util.*

class FailedAssertionResult<V, out T> private constructor(
	private val id: UUID,
	val value: V,
	val assertion: Assertion<*>,
	override val result: Validation,
) : AssertionResult<T>
{
	constructor(
		value: V,
		assertion: Assertion<*>,
		result: Validation,
	) : this(UUID.randomUUID(), value, assertion, result)
	
	override fun <R> validate(validator: Validator<T>, mapper: (T) -> R): AssertionResult<R> =
		FailedAssertionResult(id, value, assertion, result)
	
	override fun <R> take(mapper: (T) -> R): AssertionResult<R> =
		FailedAssertionResult(id, value, this, result)
	
	override fun <R> map(mapper: (T) -> R): AssertionResult<R> =
		FailedAssertionResult(id, value, this, result)
	
	override fun hashCode(): Int =
		id.hashCode()
	
	override fun equals(other: Any?): Boolean =
		other is FailedAssertionResult<*, *> && other.id == id
}
