package unittest.core.assertions

import unittest.core.LocalTestModule
import unittest.core.models.TestCase
import org.junit.Test

class OneMatchTests : LocalTestModule()
{
	private val successValues = listOf(-1, 2, -3)
	private val failureValues = listOf(-1, 2, 3)
	private val failureValuesNone = listOf(-1, -2, -3)
	
	private fun TestCase.test(value: Assertion<Int>): AssertionResult<Int> =
		value.isGreaterThan(0)
	
	private fun fail(): Nothing =
		throw Exception("unexpected exception")
	
	@Test
	fun `assert that oneMatch, on success, gives summarized results for the last element in the collection`()
	{
		val result = test {
			assertThat(successValues)
				.oneMatch { test(it) }
		}
		
		assert {
			assertThat(result)
				.assert("is success") { it.isSuccess() }
				.property { ::testResults }
				.map { it[1] }
				.property { ::message }
				.isEqualTo("validating value 2")
		}
	}
	
	@Test
	fun `assert that oneMatch, on failure, gives summarized results for the multiple element that were processed`()
	{
		val result = test {
			assertThat(failureValues)
				.oneMatch { test(it) }
		}
		
		assert {
			assertThat(result)
				.assert("is failure") { it.isSuccess().not() }
				.property { ::testResults }
				.map { it[1] }
				.property { ::message }
				.isEqualTo("validating value 3")
		}
	}
	
	@Test
	fun `assert that oneMatch, on failure, gives summarized results for the last element that was processed`()
	{
		val result = test {
			assertThat(failureValuesNone)
				.oneMatch { test(it) }
		}
		
		assert {
			assertThat(result)
				.assert("is failure") { it.isSuccess().not() }
				.property { ::testResults }
				.map { it[1] }
				.property { ::message }
				.isEqualTo("validating value -3")
		}
	}
	
	@Test
	fun `assert that oneMatch, on failure, gives a summary message for the multiple succeeding elements`()
	{
		val result = test {
			assertThat(failureValues)
				.oneMatch { test(it) }
		}
		
		assert {
			assertThat(result)
				.assert("is failure") { it.isSuccess().not() }
				.property { ::testResults }
				.map { it[3] }
				.property { ::message }
				.isEqualTo(
					"""
						|	'oneMatch { ... }' assertion failed:
						|		matches: it[1] & it[2]
						|		size:    3
					""".trimMargin("|")
				)
		}
	}
	
	@Test
	fun `assert that oneMatch, on failure, gives a summary message after validating the failing elements`()
	{
		val result = test {
			assertThat(failureValuesNone)
				.oneMatch { test(it) }
		}
		
		assert {
			assertThat(result)
				.assert("is failure") { it.isSuccess().not() }
				.property { ::testResults }
				.map { it[3] }
				.property { ::message }
				.isEqualTo(
					"""
						|	'oneMatch { ... }' assertion failed:
						|		size: 3
					""".trimMargin("|")
				)
		}
	}
	
	@Test
	fun `assert that oneMatch, on success, gives a default message`()
	{
		val result = test {
			assertThat(successValues)
				.oneMatch { test(it) }
		}
		
		assert {
			assertThat(result)
				.assert("is success") { it.isSuccess() }
				.property { ::testResults }
				.map { it[3] }
				.property { ::message }
				.isEqualTo(
					"""
						|	'oneMatch { ... }' assertion succeeded
					""".trimMargin("|")
				)
		}
	}
	
	@Test
	fun `assert that oneMatch, on failure, by unexpected exception, throws the exception`()
	{
		val result = test {
			assertThat(successValues)
				.oneMatch { fail() }
		}
		
		assert {
			assertThat(result)
				.assert("is failure") { it.isSuccess().not() }
				.property { ::testResults }
				.map { it[2] }
				.property { ::message }
				.startsWith("Unexpected error occurred while testing: java.lang.Exception: unexpected exception")
		}
	}
	
	@Test
	fun `assert that oneMatch, on success, only consumes the feed iterable once`()
	{
		var consumptions = 0
		val lazyValues = Iterable {
			iterator {
				consumptions++
				yield(-1)
				yield(2)
				yield(-3)
			}
		}
		
		val result = test {
			assertThat(lazyValues)
				.oneMatch { test(it) }
		}
		
		assert {
			assertThat(consumptions)
				.isEqualTo(1)
			
			assertThat(result)
				.assert("is success") { it.isSuccess() }
				.property { ::testResults }
				.map { it[0] }
				.property { ::message }
				.isEqualTo("validating value Iterable<Integer> { ... }")
		}
	}
	
	@Test
	fun `assert that oneMatch, on success, only consumes the feed sequence once`()
	{
		var consumptions = 0
		val lazyValues = sequence {
			consumptions++
			yield(-1)
			yield(2)
			yield(-3)
		}
		
		val result = test {
			assertThat(lazyValues)
				.oneMatch { test(it) }
		}
		
		assert {
			assertThat(result)
				.assert("is success") { it.isSuccess() }
			
			assertThat(consumptions)
				.isEqualTo(1)
			
			assertThat(result)
				.property { ::testResults }
				.map { it[0] }
				.property { ::message }
				.isEqualTo("validating value Sequence<T> { ... }")
		}
	}
}
