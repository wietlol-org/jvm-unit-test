package me.wietlol.unittest.core.validators

import me.wietlol.unittest.core.assertions.Assertion
import me.wietlol.unittest.core.assertions.AssertionResult
import me.wietlol.unittest.core.models.TestCase
import me.wietlol.unittest.core.models.TestFailedException
import me.wietlol.unittest.core.models.TestOptions

class AllMatchValidator<T>(
	override val options: TestOptions,
	val assertion: (Assertion<T>) -> AssertionResult<T>,
	val testCase: TestCase
) : Validator<Iterable<T>>, ValidatorHelper
{
	override fun validate(value: Iterable<T>): Validation
	{
		val accumulatedResults = testCase.results
		val resetSize = accumulatedResults.size
		
		val list = value.toList()
		
		val results: List<AssertionResult<T>> = list
			.asSequence()
			.map { element ->
				runCatching {
					reset(accumulatedResults, resetSize)
					val x = with(testCase) { assertThat(element) }
					val r = assertion(x)
					r
				}
			}
			.takeWhile { it.isSuccess || it.exceptionOrNull() !is TestFailedException }
			.map { it.getOrThrow() }
			.toList()
		val isValid = results.size == list.size
		val message = generateMessage(list, results, isValid)
		return Validation(isValid, message)
	}
	
	private fun generateMessage(value: List<T>, results: List<AssertionResult<T>>, isValid: Boolean): String
	{
		if (isValid)
			return "'allMatch()' assertion succeeded on all (${value.size}) elements"
		else
			return "'allMatch()' assertion failed for element at 'it[${results.size}]' (size: ${value.size})"
	}
	
	private fun reset(list: MutableList<*>, size: Int)
	{
		while (list.size > size)
			list.removeLast()
	}
}
