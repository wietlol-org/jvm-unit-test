package me.wietlol.unittest.core

import me.wietlol.unittest.core.models.TestModule
import me.wietlol.unittest.core.models.TestOptions
import java.time.Month.APRIL
import java.time.format.DateTimeFormatter

object Main
{
	@JvmStatic
	fun main(args: Array<String>)
	{
		val options = TestOptions(
			trueOptions = listOf("true", "yes", "y", "ja", "j"),
			falseOptions = listOf("false", "no", "n", "nee"),
			dateFormatters = listOf(DateTimeFormatter.ofPattern("yyyyMMdd")),
		)
		TestModule(options)
//			.test("sample test") {
			.test {
				val text: String? = "Hello, World!"
				
				assertThrows<Exception> { options.trueOptions[10] }
					.also {
						it.isInstanceOf<ArrayIndexOutOfBoundsException>()
						
						it.property { ::message }
							.isNotNull()
							.isEqualTo("Index 10 out of bounds for length 5", false)
						
						it.function { ::getLocalizedMessage }
							.isNotNull()
							.isEqualTo("Index 10 out of bounds for length 5", false)
					}
				
				assertThat(text)
					.isNotNull()
					.startsWith("hello", ignoreCase = true)
					.endsWith("world!", ignoreCase = true)
					.isEqualTo("hello, world!", ignoreCase = true)
					.matches(Regex("^[Hh]ello,\\s[Ww]orld!$"))
					.contains(", ")
				
				assertThat("123")
					.toInteger()
					.isLessThan(124)
					.isLessThanOrEqualTo(123)
					.isEqualTo(123)
					.isGreaterThanOrEqualTo(123)
					.isGreaterThan(122)
				
				assertThat("yes")
					.toBoolean()
					.isTrue()
				
				assertThat("20210401")
					.toDate()
					.assert("april fools day") { it.dayOfMonth == 1 && it.month == APRIL }
			}
			.also { println(it.toJson()) }
			.also { println(it) }
	}
}
