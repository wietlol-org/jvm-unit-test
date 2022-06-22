package me.wietlol.unittest.core

import me.wietlol.unittest.core.models.TestModule
import org.junit.Test

class ReportFormatTest : TestModule
{
	@Test
	fun `assert that texts with newlines are trimmed to retain format`()
	{
		val report = test {
			assertThat("multi\nline\nstring")
				.isEqualTo("multi\nline\nstr")
		}.toString()
		
		val reliableReport = report
			.replace(Regex("Total time: \\d+\\.\\d{3} seconds"), "Total time: 0.000 seconds")
		
		assert {
			val expectedString = StringBuilder().apply {
				appendLine("Test results for case: me.wietlol.unittest.core.ReportFormatTest::assert that texts with newlines are trimmed to retain format")
				appendLine("")
				appendLine("Summary:")
				appendLine("Total time: 0.000 seconds")
				appendLine("Total results: 2")
				appendLine("Successful results: 1")
				appendLine("Failed results: 1")
				appendLine()
				appendLine("Results:")
				appendLine("""validating value "multi\nline\nstring"""")
				appendLine("""	'isEqualTo(base="multi\nline\nstr", ignoreCase=false)' assertion failed:""")
				appendLine("""		value: "multi\nline\nstring"""")
				appendLine("""		base:  "multi\nline\nstr"""")
				appendLine("""		error:                  ^""")
			}.toString()
			
			assertThat(reliableReport)
				.isEqualTo(expectedString)
		}
	}
}
