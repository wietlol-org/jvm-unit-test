package unittest.core

import org.junit.Test

class AutomaticNamingTest : LocalTestModule()
{
	@Test
	fun `assert that the name of the function is used in the report`()
	{
		val result = test {}
		
		assert {
			assertThat(result.case.name)
				.isEqualTo("unittest.core.AutomaticNamingTest::assert that the name of the function is used in the report", false)
		}
	}
}
