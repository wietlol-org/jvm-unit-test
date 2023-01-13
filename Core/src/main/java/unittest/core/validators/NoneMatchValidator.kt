package unittest.core.validators

import unittest.core.assertions.Assertion
import unittest.core.assertions.AssertionResult
import unittest.core.models.TestCase
import unittest.core.models.TestFailedException
import unittest.core.models.TestOptions

class NoneMatchValidator<T>(
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
		
		var matchIndex = -1
		val results: List<AssertionResult<T>> = list
			.asSequence()
			.mapIndexed { i, element ->
				matchIndex = i
				runCatching {
					reset(accumulatedResults, resetSize)
					assertion(testCase.assertThat(element))
				}
			}
			.dropWhile { it.isFailure && it.exceptionOrNull() is TestFailedException }
			.take(1)
			.map { it.getOrThrow() }
			.toList()
		
		val isValid = results.isEmpty()
		if (isValid)
		{
			// adjust results to assume they failed successfully
			(resetSize until accumulatedResults.size).forEach { index ->
				val result = accumulatedResults[index]
				accumulatedResults[index] = result.copy(
					isValid = true,
					message = result.message.replace(" assertion failed", " assertion failed successfully"),
				)
			}
		}
		
		val message = generateMessage(list, matchIndex, isValid)
		return Validation(isValid, message)
	}
	
	private fun generateMessage(value: List<T>, index: Int, isValid: Boolean): String =
		if (isValid)
			"$messageIndent'noneMatch { ... }' assertion succeeded"
		else
			"$messageIndent'noneMatch { ... }' assertion failed:\n" +
				"${subMessageIndent}match: it[$index]\n" +
				"${subMessageIndent}size:  ${value.size}"
	
	private fun reset(list: MutableList<*>, size: Int)
	{
		while (list.size > size)
			list.removeLast()
	}
}
