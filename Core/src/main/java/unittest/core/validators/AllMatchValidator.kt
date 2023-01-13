package unittest.core.validators

import unittest.core.assertions.Assertion
import unittest.core.assertions.AssertionResult
import unittest.core.models.TestCase
import unittest.core.models.TestFailedException
import unittest.core.models.TestOptions

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
					assertion(testCase.assertThat(element))
				}
			}
			.takeWhile { it.isSuccess || it.exceptionOrNull() !is TestFailedException }
			.map { it.getOrThrow() }
			.toList()
		val isValid = results.size == list.size
		val message = generateMessage(list, results, isValid)
		return Validation(isValid, message)
	}
	
	private fun generateMessage(value: List<T>, results: List<AssertionResult<T>>, isValid: Boolean): String =
		if (isValid)
			"$messageIndent'allMatch { ... }' assertion succeeded"
		else
			"$messageIndent'allMatch { ... }' assertion failed:\n" +
				"${subMessageIndent}element: it[${results.size}]\n" +
				"${subMessageIndent}size:    ${value.size}"
	
	private fun reset(list: MutableList<*>, size: Int)
	{
		while (list.size > size)
			list.removeLast()
	}
}
