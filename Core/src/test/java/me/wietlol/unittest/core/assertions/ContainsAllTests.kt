package me.wietlol.unittest.core.assertions

import me.wietlol.unittest.core.LocalTestModule
import org.junit.Test

class ContainsAllTests : LocalTestModule()
{
	@Test
	fun `assert that containsAll, succeeds for equal value searches`()
	{
		val result = test {
			assertThat(listOf(0, 0, 0))
				.containsAll(0, 0, 0)
		}
		
		assert {
			assertThat(result)
				.assert("is success") { it.isSuccess() }
				.property { ::testResults }
				.map { it[1] }
				.property { ::message }
				.isEqualTo(
					"""
						|	'containsAll(needles=[0,0,0])' assertion succeeded
					""".trimMargin("|")
				)
		}
	}
	
	@Test
	fun `assert that containsAll, succeeds for equal value searches even with more input values`()
	{
		val result = test {
			assertThat(listOf(0))
				.containsAll(0, 0, 0)
		}
		
		assert {
			assertThat(result)
				.assert("is success") { it.isSuccess() }
				.property { ::testResults }
				.map { it[1] }
				.property { ::message }
				.isEqualTo(
					"""
						|	'containsAll(needles=[0,0,0])' assertion succeeded
					""".trimMargin("|")
				)
		}
	}
	
	@Test
	fun `assert that containsAll, succeeds for equal value searches for a subset of values`()
	{
		val result = test {
			assertThat(listOf(1, 2, 3))
				.containsAll(1)
		}
		
		assert {
			assertThat(result)
				.assert("is success") { it.isSuccess() }
				.property { ::testResults }
				.map { it[1] }
				.property { ::message }
				.isEqualTo(
					"""
						|	'containsAll(needles=[1])' assertion succeeded
					""".trimMargin("|")
				)
		}
	}
	
	@Test
	fun `assert that containsAll, succeeds regardless of order`()
	{
		val result = test {
			assertThat(listOf(1, 2, 3))
				.containsAll(3, 2, 1)
		}
		
		assert {
			assertThat(result)
				.assert("is success") { it.isSuccess() }
				.property { ::testResults }
				.map { it[1] }
				.property { ::message }
				.isEqualTo(
					"""
						|	'containsAll(needles=[3,2,1])' assertion succeeded
					""".trimMargin("|")
				)
		}
	}
	
	@Test
	fun `assert that containsAll, fails for non-equal value searches`()
	{
		val result = test {
			assertThat(listOf(1, 2, 3))
				.containsAll(0)
		}
		
		assert {
			assertThat(result)
				.assert("is failure") { it.isSuccess().not() }
				.property { ::testResults }
				.map { it[1] }
				.property { ::message }
				.isEqualTo(
					"""
						|	'containsAll(needles=[0])' assertion failed:
						|		needles: [0]
						|		missing: [0]
					""".trimMargin("|")
				)
		}
	}
	
	@Test
	fun `assert that containsAll, on success, only consumes the feed sequence once`()
	{
		var consumptions = 0
		val lazyValues = sequence {
			consumptions++
			yield(0)
			yield(0)
			yield(0)
		}
		val result = test {
			assertThat(lazyValues)
				.containsAll(0)
				.containsAll(0)
				.containsAll(0)
		}
		
		assert {
			assertThat(consumptions)
				.isEqualTo(1)
			
			assertThat(result)
				.assert("is success") { it.isSuccess() }
		}
	}
	
	@Test
	fun `assert that containsAll, on success, can handle infinite sequences`()
	{
		var consumptions = 0
		val lazyValues = sequence {
			consumptions++
			var l = 0L
			while (true)
				yield(l++)
		}
		val result = test {
			assertThat(lazyValues)
				.containsAll(0, 1, 2, 3, 4, 5)
		}
		
		assert {
			assertThat(consumptions)
				.isEqualTo(1)
			
			assertThat(result)
				.assert("is success") { it.isSuccess() }
		}
	}
}
