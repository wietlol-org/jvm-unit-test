package me.wietlol.unittest.core.assertions

import me.wietlol.unittest.core.LocalTestModule
import me.wietlol.unittest.core.models.TestModule
import me.wietlol.unittest.core.models.TestOptions
import org.junit.Test

class BaseTests : LocalTestModule()
{
	@Test
	fun `assert that assertThat throws an exception if it is not followed by validation calls`() = unitTest {
		assertThrows<AssertionError> {
			HiddenTestModule().unitTest { assertThat(false) }
		}
			.property { ::message }
			.isNotNull()
			.startsWith("Test case has no validations: ")

		assertThrows<AssertionError> {
			HiddenTestModule().unitTest { assertThat(true) }
		}
			.property { ::message }
			.isNotNull()
			.startsWith("Test case has no validations: ")
	}
	
	class HiddenTestModule : TestModule
	{
		override val options = TestOptions(output = null)
	}
}
