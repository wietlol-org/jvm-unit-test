package unittest.core.models

import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.databind.json.JsonMapper
import unittest.core.validators.Validation
import java.time.Duration
import java.time.LocalTime
import java.time.format.DateTimeFormatter

class TestReport(
	val case: TestCase,
	val testResults: List<Validation>,
	val duration: Duration,
	@JsonIgnore
	val mapper: JsonMapper,
)
{
	fun toDynamic(): Map<String, Any> =
		mapOf(
			"name" to case.name,
			"summary" to mapOf(
				"durationMillis" to duration.toMillis(),
				"duration" to format(duration),
				"total" to testResults.size,
				"success" to testResults.count { it.isValid },
				"failed" to testResults.count { it.isValid.not() },
			),
			"results" to testResults,
		)
	
	fun toJson(): String =
		mapper.writeValueAsString(toDynamic())
	
	override fun toString(): String =
		StringBuilder().apply {
			append("Test results for case: ")
			appendLine(case.name)
			appendLine()
			
			appendLine("Summary:")
			append("Total time: ")
			appendLine(format(duration))
			append("Total results: ")
			appendLine(testResults.size)
			append("Successful results: ")
			appendLine(testResults.count { it.isValid })
			append("Failed results: ")
			appendLine(testResults.count { it.isValid.not() })
			appendLine()
			
			appendLine("Results:")
			testResults.forEach {
				appendLine(it.message)
			}
		}.toString()
	
	fun assertSuccess()
	{
		case.options.output?.println(toString())
		if (isIndecisive())
		{
			case.options.output?.apply {
				println("This test is indecisive as it has no validations applied on the values that it tests against.")
				if (testResults.isEmpty())
				{
					println("This is probably because the test code does not contain any assertions or the assertions did not run due to conditional flow or lazy processing.")
				}
				else
				{
					println("This is probably because the test only contains assertions like the following:")
					println("\tassertThat(true)")
					println("\tassertThat(false)")
					println("\tassertThat(myBoolean)")
					println()
					println("The assertThat(value) function only specifies that this value is a value to apply tests on, but does no validation by itself.")
					println("Validating booleans works the same as every other value, which means that you have to follow up on the assertThat(value) call with a validation call.")
					println("In the case of validating booleans, following up with an .isTrue() or .isFalse() is probably sufficient.")
				}
			}
			
			throw AssertionError("Test case has no validations: ${case.name}.")
		}
		
		if (!isSuccess())
			throw AssertionError("Test case failed: ${case.name}.")
	}
	
	// if only assertThat() is called
	fun isIndecisive(): Boolean =
		testResults.all { it.message.startsWith("validating value ") }
	
	fun isSuccess(): Boolean =
		!isIndecisive() && testResults.all { it.isValid }
	
	private fun format(duration: Duration): String
	{
		val formatter = when
		{
			duration.toHours() > 0 -> DateTimeFormatter.ofPattern("H:mm:ss.SSS")
			duration.toMinutes() > 0 -> DateTimeFormatter.ofPattern("m:ss.SSS")
			else -> DateTimeFormatter.ofPattern("s.SSS' seconds'")
		}
		return LocalTime.of(0, 0, 0)
			.plus(duration)
			.format(formatter)
	}
}
