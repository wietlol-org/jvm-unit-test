package me.wietlol.unittest.core.validators

import me.wietlol.unittest.core.models.TestOptions

class ConvertToBooleanValidator(
	override val options: TestOptions,
	val trueOptions: List<String> = options.trueOptions,
	val falseOptions: List<String> = options.falseOptions,
) : Validator<CharSequence>, ValidatorHelper
{
	override fun validate(value: CharSequence): Validation
	{
		val valueText = value.toRawJsonString()
		val parsed = parseExact(valueText)
		val isValid = parsed.isSuccess
		val message = generateMessage(valueText, isValid, parsed)
		return Validation(isValid, message)
	}
	
	fun parse(value: CharSequence): Result<Boolean> =
		parseExact(value.toRawJsonString())
	
	fun parseExact(valueText: String): Result<Boolean>
	{
		val isTrue = trueOptions.any { valueText.equals(it, true) }
		val isFalse = falseOptions.any { valueText.equals(it, true) }
		val isValid = isTrue != isFalse
		return kotlin.runCatching {
			when
			{
				isValid.not() -> throw IllegalArgumentException("Value '$valueText' is not recognized as a valid boolean.")
				else -> isTrue
			}
		}
	}
	
	private fun generateMessage(valueText: String, isValid: Boolean, value: Result<Boolean>): String =
		if (isValid)
			"$messageIndent'toBoolean()' assertion succeeded (value: ${value.getOrNull()})"
		else
		{
			val valueDisplay = valueText.toJson()
			
			"$messageIndent'toBoolean()' assertion failed:\n" +
				"${subMessageIndent}value: $valueDisplay\n" +
				"${subMessageIndent}true options:  ${trueOptions.toJson()}\n" +
				"${subMessageIndent}false options: ${falseOptions.toJson()}"
		}
}
