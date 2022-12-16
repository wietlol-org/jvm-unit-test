package me.wietlol.unittest.core.assertions

import me.wietlol.unittest.core.LocalTestModule
import org.junit.Test

class ContainsExactlyTests : LocalTestModule()
{
	@Test
	fun `assert that containsExactly, succeeds for equal value searches`()
	{
		val result = test {
			assertThat(listOf(0, 0, 0))
				.containsExactly(0, 0, 0)
		}
		
		assert {
			assertThat(result)
				.assert("is success") { it.isSuccess() }
				.property { ::testResults }
				.map { it[1] }
				.property { ::message }
				.isEqualTo(
					"""
						|	'containsExactly(needles=[0,0,0])' assertion succeeded
					""".trimMargin("|")
				)
		}
	}
	
	@Test
	fun `assert that containsExactly, succeeds for empty collections`()
	{
		val result = test {
			assertThat(listOf<Int>())
				.containsExactly()
		}
		
		assert {
			assertThat(result)
				.assert("is success") { it.isSuccess() }
				.property { ::testResults }
				.map { it[1] }
				.property { ::message }
				.isEqualTo(
					"""
						|	'containsExactly(needles=[])' assertion succeeded
					""".trimMargin("|")
				)
		}
	}
	
	@Test
	fun `assert that containsExactly, succeeds regardless of order`()
	{
		val result = test {
			assertThat(listOf(1, 2, 3))
				.containsExactly(3, 2, 1)
		}
		
		assert {
			assertThat(result)
				.assert("is success") { it.isSuccess() }
				.property { ::testResults }
				.map { it[1] }
				.property { ::message }
				.isEqualTo(
					"""
						|	'containsExactly(needles=[3,2,1])' assertion succeeded
					""".trimMargin("|")
				)
		}
	}
	
	@Test
	fun `assert that containsExactly, fails if the search has more identical items`()
	{
		val result = test {
			assertThat(listOf(0, 0, 0))
				.containsExactly(0, 0, 0, 0, 0)
		}
		
		assert {
			assertThat(result)
				.assert("is failure") { it.isSuccess().not() }
				.property { ::testResults }
				.map { it[1] }
				.property { ::message }
				.isEqualTo(
					"""
						|	'containsExactly(needles=[0,0,0,0,0])' assertion failed:
						|		needles:   [0,0,0,0,0]
						|		missing:   [0,0]
						|		remaining: []
					""".trimMargin("|")
				)
		}
	}
	
	@Test
	fun `assert that containsExactly, fails if the collection has more items`()
	{
		val result = test {
			assertThat(listOf(0, 0, 0, 0, 0))
				.containsExactly(0, 0, 0)
		}
		
		assert {
			assertThat(result)
				.assert("is failure") { it.isSuccess().not() }
				.property { ::testResults }
				.map { it[1] }
				.property { ::message }
				.isEqualTo(
					"""
						|	'containsExactly(needles=[0,0,0])' assertion failed:
						|		needles:   [0,0,0]
						|		missing:   []
						|		remaining: [0,0]
					""".trimMargin("|")
				)
		}
	}
	
	@Test
	fun `assert that containsExactly, fails if the collection has non-equal items`()
	{
		val result = test {
			assertThat(listOf(1, 2, 3))
				.containsExactly(1, 4, 3)
		}
		
		assert {
			assertThat(result)
				.assert("is failure") { it.isSuccess().not() }
				.property { ::testResults }
				.map { it[1] }
				.property { ::message }
				.isEqualTo(
					"""
						|	'containsExactly(needles=[1,4,3])' assertion failed:
						|		needles:   [1,4,3]
						|		missing:   [4]
						|		remaining: [2]
					""".trimMargin("|")
				)
		}
	}
	
	@Test
	fun `assert that containsExactly, on success, only consumes the feed sequence once`()
	{
		var consumptions = 0
		val lazyValues = sequence {
			consumptions++
			yield(0)
		}
		val result = test {
			assertThat(lazyValues)
				.containsExactly(0)
				.containsExactly(0)
				.containsExactly(0)
		}
		
		assert {
			assertThat(consumptions)
				.isEqualTo(1)
			
			assertThat(result)
				.assert("is success") { it.isSuccess() }
		}
	}
	
	@Test
	fun `assert that containsExactly, on success, can handle infinite sequences`()
	{
		val lazyValues = generateSequence { 0 }
		
		
		val result = test {
			assertThat(lazyValues)
				.containsExactly(0, 0, 0)
		}
		
		assert {
			assertThat(result)
				.assert("is success") { it.isSuccess() }
				.property { ::testResults }
				.map { it[1] }
				.property { ::message }
				.isEqualTo(
					"""
						|	'containsExactly(needles=[0,0,0])' assertion succeeded
					""".trimMargin("|")
				)
		}
	}
	
	@Test
	fun `assert that containsExactly, on failure, can handle infinite sequences`()
	{
		val lazyValues = generateSequence { 0 }
		
		
		val result = test {
			assertThat(lazyValues)
				.containsExactly(1, 1, 1)
		}
		
		assert {
			assertThat(result)
				.assert("is failure") { it.isSuccess().not() }
				.property { ::testResults }
				.map { it[1] }
				.property { ::message }
				.isEqualTo(
					"""
						|	'containsExactly(needles=[1,1,1])' assertion failed:
						|		needles: [1,1,1]
						|		missing: [1,1,1]
						|		hasMore: true
					""".trimMargin("|")
				)
		}
	}
}
