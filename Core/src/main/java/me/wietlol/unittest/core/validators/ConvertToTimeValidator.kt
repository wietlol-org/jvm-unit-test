package me.wietlol.unittest.core.validators

import me.wietlol.unittest.core.models.TestOptions
import java.time.LocalTime
import java.time.format.DateTimeFormatter

class ConvertToTimeValidator(
	override val options: TestOptions,
	val formats: List<DateTimeFormatter> = options.timeFormatters,
) : Validator<CharSequence>, ValidatorHelper
{
	override fun validate(value: CharSequence): Validation
	{
		val newValue = parse(value)
		val isValid = newValue.isSuccess
		val message = generateMessage(value, isValid, newValue)
		return Validation(isValid, message)
	}
	
	fun parse(value: CharSequence): Result<LocalTime>
	{
		val valueText = value.toString()
		val error = IllegalArgumentException("Value '$valueText' is not recognized as a valid time.")
		return formats
			.asSequence()
			.map { runCatching { LocalTime.parse(valueText, it) } }
			.onEach { if (it.isFailure) error.addSuppressed(it.exceptionOrNull()!!) }
			.filter { it.isSuccess }
			.firstOrNull()
			?: Result.failure(error)
	}
	
	private fun generateMessage(value: CharSequence, isValid: Boolean, newValue: Result<LocalTime>): String =
		if (isValid)
			"$messageIndent'toTime()' assertion succeeded (value: ${newValue.getOrThrow()})"
		else
		{
			val valueDisplay = value.toJson()
			
			"$messageIndent'toTime()' assertion failed:\n" +
				"${subMessageIndent}value: $valueDisplay\n" +
				"${subMessageIndent}error: ${newValue.exceptionOrNull()?.toString()}"
		}
}
