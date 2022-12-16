package me.wietlol.unittest.core.validators

import me.wietlol.unittest.core.LocalTestModule
import me.wietlol.unittest.core.models.TestOptions
import org.junit.Test

class StringEqualsValidatorTest : LocalTestModule()
{
	@Test
	fun `assert that validating an empty string, does not throw an exception`()
	{
		val value = ""
		val base = "test"
		
		val validator = StringEqualValidator(TestOptions(), base, true)
		val result = validator.validate(value)
		
		assert {
			assertThat(result.message).isEqualTo(
				"""
					|	'isEqualTo(base="test", ignoreCase=true)' assertion failed:
					|		value: ""
					|		base:  "test"
					|		error:  ^
				""".trimMargin("|")
			)
		}
	}
	
	@Test
	fun `assert waddaheckinshite`()
	{
		val value = "'allMatch()' assertion failed for element at 1 (out of 3)"
		val base = "'allMatch()' assertion failed for element at 'it[1]' (size: 3)"
		
		val validator = StringEqualValidator(TestOptions(), base, false)
		val result = validator.validate(value)
		
		assert {
			assertThat(result.message).isEqualTo(
				"""
					|	'isEqualTo(base="'allMatch()' assertion failed for element at 'it[1]' (size: 3)", ignoreCase=false)' assertion failed:
					|		value: "'allMatch()' assertion failed for element at 1 (out of 3)"
					|		base:  "'allMatch()' assertion failed for element at 'it[1]' (size: 3)"
					|		error:                                               ^
				""".trimMargin("|")
			)
		}
	}
}
