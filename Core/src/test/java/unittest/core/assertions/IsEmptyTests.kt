package unittest.core.assertions

import unittest.core.LocalTestModule
import org.junit.Test

class IsEmptyTests : LocalTestModule()
{
	@Test
	fun `assert that isEmpty succeeds on empty collections`()
	{
		val result = test {
			assertThat(listOf<Int>())
				.isEmpty()
		}
		
		assert {
			assertThat(result)
				.assert("is success") { it.isSuccess() }
		}
	}
	
	@Test
	fun `assert that isNotEmpty succeeds on non-empty collections`()
	{
		val result = test {
			assertThat(listOf(0))
				.isNotEmpty()
		}
		
		assert {
			assertThat(result)
				.assert("is success") { it.isSuccess() }
		}
	}
	
	@Test
	fun `assert that isEmpty succeeds on empty sequences`()
	{
		val result = test {
			assertThat(sequenceOf<Int>())
				.isEmpty()
		}
		
		assert {
			assertThat(result)
				.assert("is success") { it.isSuccess() }
		}
	}
	
	@Test
	fun `assert that isNotEmpty succeeds on non-empty sequences`()
	{
		val result = test {
			assertThat(sequenceOf(0))
				.isNotEmpty()
		}
		
		assert {
			assertThat(result)
				.assert("is success") { it.isSuccess() }
		}
	}
	
	@Test
	fun `assert that isEmpty fails on non-empty collections`()
	{
		val result = test {
			assertThat(listOf(0))
				.isEmpty()
		}
		
		assert {
			assertThat(result)
				.assert("is failure") { it.isSuccess().not() }
		}
	}
	
	@Test
	fun `assert that isNotEmpty fails on empty collections`()
	{
		val result = test {
			assertThat(listOf<Int>())
				.isNotEmpty()
		}
		
		assert {
			assertThat(result)
				.assert("is failure") { it.isSuccess().not() }
		}
	}
	
	@Test
	fun `assert that isEmpty fails on non-empty sequences`()
	{
		val result = test {
			assertThat(sequenceOf(0))
				.isEmpty()
		}
		
		assert {
			assertThat(result)
				.assert("is failure") { it.isSuccess().not() }
		}
	}
	
	@Test
	fun `assert that isNotEmpty fails on empty sequences`()
	{
		val result = test {
			assertThat(sequenceOf<Int>())
				.isNotEmpty()
		}
		
		assert {
			assertThat(result)
				.assert("is failure") { it.isSuccess().not() }
		}
	}
	
	@Test
	fun `assert that isEmpty, on success, prints a default assertion message`()
	{
		val result = test {
			assertThat(listOf<Int>())
				.isEmpty()
		}
		
		assert {
			assertThat(result)
				.assert("is success") { it.isSuccess() }
				.property { ::testResults }
				.map { it[1] }
				.property { ::message }
				.isEqualTo(
					"""
						|	'isEmpty()' assertion succeeded
					""".trimMargin("|")
				)
		}
	}
	
	@Test
	fun `assert that isEmpty, on failure, tells how many items there are in the collection`()
	{
		val result = test {
			assertThat(listOf(0, 0, 0))
				.isEmpty()
		}
		
		assert {
			assertThat(result)
				.assert("is failure") { it.isSuccess().not() }
				.property { ::testResults }
				.map { it[1] }
				.property { ::message }
				.isEqualTo(
					"""
						|	'isEmpty()' assertion failed:
						|		size: 3
					""".trimMargin("|")
				)
		}
	}
	
	@Test
	fun `assert that isNotEmpty, on success, prints a default assertion message`()
	{
		val result = test {
			assertThat(listOf(0, 0, 0))
				.isNotEmpty()
		}
		
		assert {
			assertThat(result)
				.assert("is success") { it.isSuccess() }
				.property { ::testResults }
				.map { it[1] }
				.property { ::message }
				.isEqualTo(
					"""
						|	'isNotEmpty()' assertion succeeded
					""".trimMargin("|")
				)
		}
	}
	
	@Test
	fun `assert that isEmpty, on failure, works with infinite sequences`()
	{
		val result = test {
			assertThat(generateSequence { 0 })
				.isEmpty()
		}
		
		assert {
			assertThat(result)
				.assert("is failure") { it.isSuccess().not() }
				.property { ::testResults }
				.map { it[1] }
				.property { ::message }
				.isEqualTo(
					"""
						|	'isEmpty()' assertion failed:
						|		type: sequence
					""".trimMargin("|")
				)
		}
	}
	
	@Test
	fun `assert that isEmpty, on success, prints a default assertion message for sequences`()
	{
		val result = test {
			assertThat(sequenceOf<Int>())
				.isEmpty()
		}
		
		assert {
			assertThat(result)
				.assert("is success") { it.isSuccess() }
				.property { ::testResults }
				.map { it[1] }
				.property { ::message }
				.isEqualTo(
					"""
						|	'isEmpty()' assertion succeeded
					""".trimMargin("|")
				)
		}
	}
	
	@Test
	fun `assert that isNotEmpty, on success, prints a default assertion message for sequences`()
	{
		val result = test {
			assertThat(sequenceOf(0))
				.isNotEmpty()
		}
		
		assert {
			assertThat(result)
				.assert("is success") { it.isSuccess() }
				.property { ::testResults }
				.map { it[1] }
				.property { ::message }
				.isEqualTo(
					"""
						|	'isNotEmpty()' assertion succeeded
					""".trimMargin("|")
				)
		}
	}
	
	@Test
	fun `assert that isEmpty, on success, consumes the sequence only once`()
	{
		var consumptions = 0
		val lazyValues = sequence {
			consumptions++
			yield(1)
			yield(2)
			yield(3)
		}
		val result = test {
			assertThat(lazyValues)
				.isNotEmpty()
				.isNotEmpty()
				.isNotEmpty()
		}
		
		assert {
			assertThat(consumptions)
				.isEqualTo(1)
			
			assertThat(result)
				.assert("is success") { it.isSuccess() }
				.property { ::testResults }
				.map { it[1] }
				.property { ::message }
				.isEqualTo(
					"""
						|	'isNotEmpty()' assertion succeeded
					""".trimMargin("|")
				)
		}
	}
}
