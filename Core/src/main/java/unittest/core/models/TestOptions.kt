package unittest.core.models

import com.fasterxml.jackson.databind.json.JsonMapper
import java.io.PrintStream
import java.time.format.DateTimeFormatter

data class TestOptions(
	val displayLength: Int = 100,
	val trueOptions: List<String> = listOf("true"),
	val falseOptions: List<String> = listOf("false"),
	val dateFormatters: List<DateTimeFormatter> = listOf(DateTimeFormatter.ISO_LOCAL_DATE),
	val timeFormatters: List<DateTimeFormatter> = listOf(DateTimeFormatter.ISO_LOCAL_TIME),
	val dateTimeFormatters: List<DateTimeFormatter> = listOf(DateTimeFormatter.ISO_LOCAL_DATE_TIME),
	val mapper: JsonMapper = JsonMapper(),
	val output: PrintStream? = System.out,
)
