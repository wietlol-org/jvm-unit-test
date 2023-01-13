package unittest.core.validators

import unittest.core.assertions.Assertion
import unittest.core.assertions.AssertionResult
import unittest.core.models.TestCase
import unittest.core.models.TestFailedException
import unittest.core.models.TestOptions

class AnyMatchValidator<T>(
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
		
		var index = -1
		val results: List<AssertionResult<T>> = list
			.asSequence()
			.mapIndexed { i, element ->
				index = i
				runCatching {
					reset(accumulatedResults, resetSize)
					assertion(testCase.assertThat(element))
				}
			}
			.dropWhile { !it.isSuccess && it.exceptionOrNull() is TestFailedException }
			.take(1)
			.map { it.getOrThrow() }
			.toList()
		
		val isValid = results.isNotEmpty()
		val message = generateMessage(list, index, isValid)
		return Validation(isValid, message)
	}
	
	private fun generateMessage(value: List<T>, index: Int, isValid: Boolean): String =
		if (isValid)
			"$messageIndent'anyMatch { ... }' assertion succeeded"
		else
			"$messageIndent'anyMatch { ... }' assertion failed:\n" +
				"${subMessageIndent}size: ${value.size}"
	
	private fun reset(list: MutableList<*>, size: Int)
	{
		while (list.size > size)
			list.removeLast()
	}
}
