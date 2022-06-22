package me.wietlol.unittest.core.assertions

import me.wietlol.unittest.core.models.TestModule
import org.junit.Test

class MapAssertionTest : TestModule
{
	@Test
	fun `assert that map{} starts a new assertion sequence`()
	{
		val value = ""
		val result = test {
			assertThat(value)
				.map { it.length }
		}
		
		assert {
			assertThat(result)
				.map { it.testResults[1].message }
				.isEqualTo("validating mapped value 0")
		}
	}
	
	@Test
	fun `assert that map{} from successful assertions, starts a new assertion sequence`()
	{
		val value = ""
		val result = test {
			assertThat(value)
				.isEmpty()
				.map { it.length }
		}
		
		assert {
			assertThat(result)
				.map { it.testResults[2].message }
				.isEqualTo("validating mapped value 0")
		}
	}
}
