package me.wietlol.unittest.core.validators

import me.wietlol.unittest.core.models.TestModule
import me.wietlol.unittest.core.models.TestOptions
import org.junit.Test

class StringEndsWithValidatorTest : TestModule
{
	@Test
	fun `assert that success message displays the tail`()
	{
		val value = "Hello, World!"
		val tail = "world!"
		
		val validator = StringEndsWithValidator(TestOptions(), tail, true)
		val result = validator.validate(value)
		
		assert {
			assertThat(result.message as CharSequence).isEqualTo(
				"""
					|	'endsWith(tail="world!", ignoreCase=true)' assertion succeeded
				""".trimMargin("|")
			)
		}
	}
	
	@Test
	fun `assert that failed message displays the index where the fail happened`()
	{
		val value = "Hello, World!"
		val tail = "wrld!"
		
		val validator = StringEndsWithValidator(TestOptions(), tail, true)
		val result = validator.validate(value)
		
		assert {
			assertThat(result.message as CharSequence).isEqualTo(
				"""
					|	'endsWith(tail="wrld!", ignoreCase=true)' assertion failed:
					|		value: "Hello, World!"
					|		tail:          "wrld!"
					|		error:          ^
				""".trimMargin("|")
			)
		}
	}
	
	@Test
	fun `assert that success message displays the tail up to 100 characters`()
	{
		val value =
			"abcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyz"
		val tail =
			"opqrstuvwxyzabcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyz"
		
		val validator = StringEndsWithValidator(TestOptions(), tail, true)
		val result = validator.validate(value)
		
		assert {
			assertThat(result.message as CharSequence).isEqualTo(
				"""
					|	'endsWith(tail="...hijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyz", ignoreCase=true)' assertion succeeded
				""".trimMargin("|")
			)
		}
	}
	
	@Test
	fun `assert that failed message displays the texts up to 100 characters`()
	{
		val value =
			"abcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyz"
		val tail =
			"opqrstuvwxyzabcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyzabcdefghijklmnoqrstuvwxyzabcdefghijklmnopqrstuvwxyz"
		
		val validator = StringEndsWithValidator(TestOptions(), tail, true)
		val result = validator.validate(value)
		
		assert {
			assertThat(result.message as CharSequence).isEqualTo(
				"""
					|	'endsWith(tail="...ghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyzabcdefghijklmnoqrstuvwxyzabcdefghijklmnopqrstuvwxyz", ignoreCase=true)' assertion failed:
					|		value: "...hijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyz"
					|		tail:  "...ghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyzabcdefghijklmnoqrstuvwxyzabcdefghijklmnopqrstuvwxyz"
					|		error:                                                                 ^
				""".trimMargin("|")
			)
		}
	}
	
	@Test
	fun `assert that failed message displays the texts up to 100 characters with the error in the center`()
	{
		val value =
			"abcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyz"
		val tail =
			"abcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyzabcdefghijlmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyz"
		
		val validator = StringEndsWithValidator(TestOptions(), tail, true)
		val result = validator.validate(value)
		
		assert {
			assertThat(result.message as CharSequence).isEqualTo(
				"""
					|	'endsWith(tail="...ghijklmnopqrstuvwxyzabcdefghijlmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyz", ignoreCase=true)' assertion failed:
					|		value: "...qrstuvwxyzabcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyzabcdef..."
					|		tail:  "...pqrstuvwxyzabcdefghijklmnopqrstuvwxyzabcdefghijlmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyzabcdef..."
					|		error:                                                   ^
				""".trimMargin("|")
			)
		}
	}
	
	@Test
	fun `assert that failed message displays the texts up to 100 characters from the start`()
	{
		val value =
			"abcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyz"
		val tail =
			"abcdefhijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyz"
		
		val validator = StringEndsWithValidator(TestOptions(), tail, true)
		val result = validator.validate(value)
		
		assert {
			assertThat(result.message as CharSequence).isEqualTo(
				"""
					|	'endsWith(tail="...hijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyz", ignoreCase=true)' assertion failed:
					|		value: "abcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrs..."
					|		tail:  "abcdefhijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrst..."
					|		error:        ^
				""".trimMargin("|")
			)
		}
	}
	
	@Test
	fun `assert that failed message correctly displays the error for longer tails`()
	{
		val value = "abcdefghijklmnopqrstuvwxyz"
		val tail = "abcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyz"
		
		val validator = StringEndsWithValidator(TestOptions(), tail, true)
		val result = validator.validate(value)
		
		assert {
			assertThat(result.message as CharSequence).isEqualTo(
				"""
					|	'endsWith(tail="abcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyz", ignoreCase=true)' assertion failed:
					|		value:                           "abcdefghijklmnopqrstuvwxyz"
					|		tail:  "abcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyz"
					|		error:                           ^
				""".trimMargin("|")
			)
		}
	}
	
	@Test
	fun `assert that special characters are displayed escaped`()
	{
		val value = "Hello,\tWorld!"
		val tail = " world!"
		
		val validator = StringEndsWithValidator(TestOptions(), tail, true)
		val result = validator.validate(value)
		
		assert {
			assertThat(result.message as CharSequence).isEqualTo(
				"""
					|	'endsWith(tail=" world!", ignoreCase=true)' assertion failed:
					|		value: "Hello,\tWorld!"
					|		tail:         " world!"
					|		error:         ^
				""".trimMargin("|")
			)
		}
	}
	
	@Test
	fun `assert that escaped characters are offset correctly`()
	{
		val value = "Hello,\tWorld!"
		val tail = "a,\tworld!"
		
		val validator = StringEndsWithValidator(TestOptions(), tail, true)
		val result = validator.validate(value)
		
		assert {
			assertThat(result.message as CharSequence).isEqualTo(
				"""
					|	'endsWith(tail="a,\tworld!", ignoreCase=true)' assertion failed:
					|		value: "Hello,\tWorld!"
					|		tail:      "a,\tworld!"
					|		error:      ^
				""".trimMargin("|")
			)
		}
	}
}
