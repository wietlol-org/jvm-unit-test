package me.wietlol.unittest.core

import me.wietlol.unittest.core.models.TestModule
import org.junit.Test

class AutomaticNamingTest : TestModule
{
	@Test
	fun `assert that the name of the function is used in the report`()
	{
		val result = test {}
		
		assert {
			assertThat(result.case.name)
				.isEqualTo("me.wietlol.unittest.core.AutomaticNamingTest::assert that the name of the function is used in the report", false)
		}
	}
}
