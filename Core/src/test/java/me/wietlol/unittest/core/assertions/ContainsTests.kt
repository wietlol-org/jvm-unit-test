package me.wietlol.unittest.core.assertions

import me.wietlol.unittest.core.LocalTestModule
import org.junit.Test

class ContainsTests : LocalTestModule()
{
	@Test
	fun `assert that contains, succeeds for equal value searches`()
	{
		val result = test {
			assertThat(listOf(0, 0, 0))
				.contains(0)
		}
		
		assert {
			assertThat(result)
				.assert("is success") { it.isSuccess() }
				.property { ::testResults }
				.map { it[1] }
				.property { ::message }
				.isEqualTo(
					"""
						|	'contains(needle=0)' assertion succeeded
					""".trimMargin("|")
				)
		}
	}
	
	@Test
	fun `assert that contains, fails for non-equal value searches`()
	{
		val result = test {
			assertThat(listOf(1, 2, 3))
				.contains(0)
		}
		
		assert {
			assertThat(result)
				.assert("is failure") { it.isSuccess().not() }
				.property { ::testResults }
				.map { it[1] }
				.property { ::message }
				.isEqualTo(
					"""
						|	'contains(needle=0)' assertion failed:
						|		size:   3
						|		needle: 0
					""".trimMargin("|")
				)
		}
	}
	
	@Test
	fun `assert that contains, fails for empty collections`()
	{
		val result = test {
			assertThat(emptyList<Int>())
				.contains(0)
		}
		
		assert {
			assertThat(result)
				.assert("is failure") { it.isSuccess().not() }
				.property { ::testResults }
				.map { it[1] }
				.property { ::message }
				.isEqualTo(
					"""
						|	'contains(needle=0)' assertion failed:
						|		size:   0
						|		needle: 0
					""".trimMargin("|")
				)
		}
	}
	
	@Test
	fun `assert that contains, on success, only consumes the feed sequence once`()
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
				.contains(0)
				.contains(0)
				.contains(0)
		}
		
		assert {
			assertThat(consumptions)
				.isEqualTo(1)
			
			assertThat(result)
				.assert("is success") { it.isSuccess() }
		}
	}
}
