package me.wietlol.unittest.core.validators

import me.wietlol.unittest.core.models.TestOptions
import java.time.LocalDate
import java.time.LocalTime
import java.time.format.DateTimeFormatter

class ConvertToDateValidator(
	override val options: TestOptions,
	val formats: List<DateTimeFormatter> = options.dateFormatters,
) : Validator<CharSequence>, ValidatorHelper
{
	override fun validate(value: CharSequence): Validation
	{
		val newValue = parse(value)
		val isValid = newValue.isSuccess
		val message = generateMessage(value, isValid, newValue)
		return Validation(isValid, message)
	}
	
	fun parse(value: CharSequence): Result<LocalDate>
	{
		val valueText = value.toString()
		val error = IllegalArgumentException("Value '$valueText' is not recognized as a valid date.")
		return formats
			.asSequence()
			.map { runCatching { LocalDate.parse(valueText, it) } }
			.onEach { if (it.isFailure) error.addSuppressed(it.exceptionOrNull()!!) }
			.filter { it.isSuccess }
			.firstOrNull()
			?: Result.failure(error)
	}
	
	private fun generateMessage(value: CharSequence, isValid: Boolean, newValue: Result<LocalDate>): String =
		if (isValid)
			"$messageIndent'toDate()' assertion succeeded (value: ${newValue.getOrThrow()})"
		else
		{
			val valueDisplay = value.toJson()
			
			"$messageIndent'toDate()' assertion failed:\n" +
				"${subMessageIndent}value: $valueDisplay\n" +
				"${subMessageIndent}error: ${newValue.exceptionOrNull()?.toString()}"
		}
}
