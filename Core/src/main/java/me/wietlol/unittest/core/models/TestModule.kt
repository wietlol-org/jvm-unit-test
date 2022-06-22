package me.wietlol.unittest.core.models

import me.wietlol.unittest.core.validators.Validation
import java.time.Duration
import kotlin.system.measureTimeMillis

interface TestModule
{
	val options: TestOptions
		get() = defaultOptions
	
	fun assert(name: String? = null, body: TestCase.() -> Unit): TestReport
	{
		val fullName = name ?: getFunctionName()
		return test(fullName, body).also { it.assertSuccess() }
	}
	
	fun unitTest(name: String? = null, body: TestCase.() -> Unit)
	{
		val fullName = name ?: getFunctionName()
		assert(fullName, body)
	}
	
	fun test(name: String? = null, body: TestCase.() -> Unit): TestReport
	{
		val fullName = name ?: getFunctionName()
		
		val case = TestCase(fullName, options)
		val results: List<Validation>
		val duration = measureTimeMillis {
			results = try
			{
				case.body()
				case.results
			}
			catch (ex: TestFailedException)
			{
				ex.failedAssertionResults
			}
			catch (ex: Throwable)
			{
				case.results + Validation(false, "Unexpected error occurred while testing: ${ex.stackTraceToString()}")
			}
		}.let(Duration::ofMillis)
		
		return TestReport(case, results, duration, options.mapper)
	}
	
	// known bug: when the caller method's name is "test" or "assert", it won't pick it up
	private fun getFunctionName(): String =
		Thread.currentThread().stackTrace
			.drop(2) // thread stack trace noise
			.drop(1) // getFunctionName
			.dropWhile { it.methodName == "test" || it.methodName == "test\$default"
				|| it.methodName == "assert" || it.methodName == "assert\$default"
				|| it.methodName == "unitTest" || it.methodName == "unitTest\$default"}
			.first()
			.let { "${it.className}::${it.methodName}" }
	
	companion object
	{
		private val defaultOptions: TestOptions = TestOptions()
		
		operator fun invoke(options: TestOptions = defaultOptions): TestModule =
			DefaultTestModule(options)
		
		private class DefaultTestModule(
			override val options: TestOptions
		) : TestModule
	}
}
