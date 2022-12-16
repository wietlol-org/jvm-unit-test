package me.wietlol.unittest.core.validators

import me.wietlol.unittest.core.assertions.Assertion
import me.wietlol.unittest.core.assertions.AssertionResult
import me.wietlol.unittest.core.models.TestCase
import me.wietlol.unittest.core.models.TestFailedException
import me.wietlol.unittest.core.models.TestOptions

class OneMatchValidator<T>(
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
		
		val indices = mutableListOf<Int>()
		var successResults = emptyList<Validation>()
		var failureResults = emptyList<Validation>()
		val results: List<AssertionResult<T>> = list
			.asSequence()
			.map { runCatching { assertion(testCase.assertThat(it)) } }
			.onEachIndexed { i, result ->
				if (result.isSuccess || result.exceptionOrNull() is TestFailedException)
				{
					val results = reset(accumulatedResults, resetSize)
					if (result.getOrNull()?.result?.isValid == true)
					{
						successResults = results
						indices += i
					}
					else
					{
						failureResults = results
					}
				}
				else
				{
					failureResults = reset(accumulatedResults, resetSize)
				}
			}
			.filter { it.isSuccess || it.exceptionOrNull() !is TestFailedException }
			.take(2)
			.onEach {
				if (it.isFailure)
					accumulatedResults.addAll(failureResults)
			}
			.map { it.getOrThrow() }
			.toList()
		
		val isValid = results.size == 1
		
		accumulatedResults.addAll(if (results.isNotEmpty()) successResults else failureResults)
		
		val message = generateMessage(list, indices, results)
		return Validation(isValid, message)
	}
	
	private fun generateMessage(value: List<T>, indices: List<Int>, results: List<*>): String =
		when (results.size)
		{
			0 -> "$messageIndent'oneMatch { ... }' assertion failed:\n" +
				"${subMessageIndent}size: ${value.size}"
			1 -> "$messageIndent'oneMatch { ... }' assertion succeeded"
			else -> "$messageIndent'oneMatch { ... }' assertion failed:\n" +
				"${subMessageIndent}matches: ${formatIndices(indices)}\n" +
				"${subMessageIndent}size:    ${value.size}"
		}
	
	private fun formatIndices(indices: List<Int>): String =
		indices.joinToString(" & ") { "it[$it]" }
	
	private fun reset(list: MutableList<Validation>, size: Int): List<Validation>
	{
		val result = list.subList(size, list.size).toList()
		
		while (list.size > size)
			list.removeLast()
		
		return result
	}
}
