package me.wietlol.unittest.core.assertions

import me.wietlol.unittest.core.HiddenTestModule
import me.wietlol.unittest.core.LocalTestModule
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
}
