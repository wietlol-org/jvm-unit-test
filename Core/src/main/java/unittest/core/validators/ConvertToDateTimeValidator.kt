package unittest.core.validators

import unittest.core.models.TestOptions
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class ConvertToDateTimeValidator(
	override val options: TestOptions,
	val formats: List<DateTimeFormatter> = options.dateTimeFormatters,
) : Validator<CharSequence>, ValidatorHelper
{
	override fun validate(value: CharSequence): Validation
	{
		val newValue = parse(value)
		val isValid = newValue.isSuccess
		val message = generateMessage(value, isValid, newValue)
		return Validation(isValid, message)
	}
	
	fun parse(value: CharSequence): Result<LocalDateTime>
	{
		val valueText = value.toString()
		val error = IllegalArgumentException("Value '$valueText' is not recognized as a valid date-time.")
		return formats
			.asSequence()
			.map { runCatching { LocalDateTime.parse(valueText, it) } }
			.onEach { if (it.isFailure) error.addSuppressed(it.exceptionOrNull()!!) }
			.filter { it.isSuccess }
			.firstOrNull()
			?: Result.failure(error)
	}
	
	private fun generateMessage(value: CharSequence, isValid: Boolean, newValue: Result<LocalDateTime>): String =
		if (isValid)
			"$messageIndent'toDateTime()' assertion succeeded (value: ${newValue.getOrThrow()})"
		else
		{
			val valueDisplay = value.toJson()
			
			"$messageIndent'toDateTime()' assertion failed:\n" +
				"${subMessageIndent}value: $valueDisplay\n" +
				"${subMessageIndent}error: ${newValue.exceptionOrNull()?.toString()}"
		}
}
