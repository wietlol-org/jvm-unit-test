package me.wietlol.unittest.core.exampletests

import me.wietlol.unittest.core.LocalTestModule
import org.junit.Test

class StringTest : LocalTestModule()
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
