package me.wietlol.unittest.core.validators

import me.wietlol.unittest.core.models.TestModule
import me.wietlol.unittest.core.models.TestOptions
import org.junit.Test

class StringStartsWithValidatorTest : TestModule
{
	@Test
	fun `assert that success message displays the head`()
	{
		val value = "Hello, World!"
		val head = "hello,"
		
		val validator = StringStartsWithValidator(TestOptions(), head, true)
		val result = validator.validate(value)
		
		assert {
			assertThat(result.message as CharSequence).isEqualTo(
				"""
					|	'startsWith(head="hello,", ignoreCase=true)' assertion succeeded
				""".trimMargin("|")
			)
		}
	}
	
	@Test
	fun `assert that failed message displays the index where the fail happened`()
	{
		val value = "Hello, World!"
		val head = "hell,"
		
		val validator = StringStartsWithValidator(TestOptions(), head, true)
		val result = validator.validate(value)
		
		assert {
			assertThat(result.message as CharSequence).isEqualTo(
				"""
					|	'startsWith(head="hell,", ignoreCase=true)' assertion failed:
					|		value: "Hello, World!"
					|		head:  "hell,"
					|		error:      ^
				""".trimMargin("|")
			)
		}
	}
	
	@Test
	fun `assert that success message displays the head up to 100 characters`()
	{
		val value =
			"abcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyz"
		val head =
			"abcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyz"
		
		val validator = StringStartsWithValidator(TestOptions(), head, true)
		val result = validator.validate(value)
		
		assert {
			assertThat(result.message as CharSequence).isEqualTo(
				"""
					|	'startsWith(head="abcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrs...", ignoreCase=true)' assertion succeeded
				""".trimMargin("|")
			)
		}
	}
	
	@Test
	fun `assert that failed message displays the texts up to 100 characters`()
	{
		val value =
			"abcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyz"
		val head =
			"abcdefgijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyz"
		
		val validator = StringStartsWithValidator(TestOptions(), head, true)
		val result = validator.validate(value)
		
		assert {
			assertThat(result.message as CharSequence).isEqualTo(
				"""
					|	'startsWith(head="abcdefgijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrst...", ignoreCase=true)' assertion failed:
					|		value: "abcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrs..."
					|		head:  "abcdefgijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrst..."
					|		error:         ^
				""".trimMargin("|")
			)
		}
	}
	
	@Test
	fun `assert that failed message displays the texts up to 100 characters with the error in the center`()
	{
		val value =
			"abcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyz"
		val head =
			"abcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyzabcdefghijlmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyz"
		
		val validator = StringStartsWithValidator(TestOptions(), head, true)
		val result = validator.validate(value)
		
		assert {
			assertThat(result.message as CharSequence).isEqualTo(
				"""
					|	'startsWith(head="abcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyzabcdefghijlmnopqrstuvwxyzabcdefghijklmnopqrst...", ignoreCase=true)' assertion failed:
					|		value: "...pqrstuvwxyzabcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyzabcde..."
					|		head:  "...pqrstuvwxyzabcdefghijklmnopqrstuvwxyzabcdefghijlmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyzabcdef..."
					|		error:                                                    ^
				""".trimMargin("|")
			)
		}
	}
	
	@Test
	fun `assert that failed message displays the texts up to 100 characters from the end`()
	{
		val value =
			"abcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyz"
		val head =
			"abcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstvwxyz"
		
		val validator = StringStartsWithValidator(TestOptions(), head, true)
		val result = validator.validate(value)
		
		assert {
			assertThat(result.message as CharSequence).isEqualTo(
				"""
					|	'startsWith(head="abcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrs...", ignoreCase=true)' assertion failed:
					|		value: "...hijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyz"
					|		head:  "...hijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstvwxyz"
					|		error:                                                                                                ^
				""".trimMargin("|")
			)
		}
	}
	
	@Test
	fun `assert that failed message correctly displays the error for longer heads`()
	{
		val value = "abcdefghijklmnopqrstuvwxyz"
		val head = "abcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyz"
		
		val validator = StringStartsWithValidator(TestOptions(), head, true)
		val result = validator.validate(value)
		
		assert {
			assertThat(result.message as CharSequence).isEqualTo(
				"""
					|	'startsWith(head="abcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyz", ignoreCase=true)' assertion failed:
					|		value: "abcdefghijklmnopqrstuvwxyz"
					|		head:  "abcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyz"
					|		error:                            ^
				""".trimMargin("|")
			)
		}
	}
	
	@Test
	fun `assert that special characters are displayed escaped`()
	{
		val value = "Hello,\tWorld!"
		val head = "hello, "
		
		val validator = StringStartsWithValidator(TestOptions(), head, true)
		val result = validator.validate(value)
		
		assert {
			assertThat(result.message as CharSequence).isEqualTo(
				"""
					|	'startsWith(head="hello, ", ignoreCase=true)' assertion failed:
					|		value: "Hello,\tWorld!"
					|		head:  "hello, "
					|		error:        ^
				""".trimMargin("|")
			)
		}
	}
	
	@Test
	fun `assert that escaped characters are offset correctly`()
	{
		val value = "Hello,\tWorld!"
		val head = "hello,\twa"
		
		val validator = StringStartsWithValidator(TestOptions(), head, true)
		val result = validator.validate(value)
		
		assert {
			assertThat(result.message as CharSequence).isEqualTo(
				"""
					|	'startsWith(head="hello,\twa", ignoreCase=true)' assertion failed:
					|		value: "Hello,\tWorld!"
					|		head:  "hello,\twa"
					|		error:           ^
				""".trimMargin("|")
			)
		}
	}
}
