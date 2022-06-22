package me.wietlol.unittest.core.exampletests

import me.wietlol.unittest.core.models.TestModule
import org.junit.Test

class StringTest : TestModule
{
	@Test
	fun foo()
	{
		assert {
			val string = "Hello, World!"
			
			assertThat(string)
				.startsWith("Hello,")
				.endsWith("World!")
				.contains(", ")
		}
	}
}
