package unittest.core.assertions

import unittest.core.LocalTestModule
import org.junit.Test

class MapAssertionTest : LocalTestModule()
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
