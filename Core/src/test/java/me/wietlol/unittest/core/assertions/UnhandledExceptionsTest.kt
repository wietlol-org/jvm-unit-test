package me.wietlol.unittest.core.assertions

import me.wietlol.unittest.core.models.TestModule
import org.junit.Test

class UnhandledExceptionsTest : TestModule
{
	@Test
	fun `assert that exceptions do not prevent reports`() = unitTest {
		val report = test {
			throw Exception()
		}
		
		assertThat(report.isSuccess())
			.isFalse()
	}
}
