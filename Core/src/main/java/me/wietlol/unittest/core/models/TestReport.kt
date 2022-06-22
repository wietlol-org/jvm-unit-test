package me.wietlol.unittest.core.models

import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.databind.json.JsonMapper
import me.wietlol.unittest.core.models.TestCase
import me.wietlol.unittest.core.validators.Validation
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
		println(toString())
		if (!isSuccess())
			throw AssertionError("Test case ${case.name} failed.")
	}
	
	fun isSuccess(): Boolean =
		testResults.all { it.isValid }
	
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
