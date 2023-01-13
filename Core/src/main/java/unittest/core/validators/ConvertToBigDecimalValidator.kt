package unittest.core.validators

import unittest.core.models.TestOptions
import java.math.BigDecimal

class ConvertToBigDecimalValidator(
	override val options: TestOptions,
) : Validator<CharSequence>, ValidatorHelper
{
	override fun validate(value: CharSequence): Validation
	{
		val newValue = parse(value)
		val message = generateMessage(value, newValue)
		return Validation(newValue.isSuccess, message)
	}
	
	fun parse(value: CharSequence): Result<BigDecimal> =
		runCatching { value.toString().toBigDecimal() }
	
	private fun generateMessage(value: CharSequence, newValue: Result<BigDecimal>): String =
		newValue
			.map { "$messageIndent'toDecimal()' assertion succeeded (value: $it)" }
			.getOrElse {
				"$messageIndent'toDecimal()' assertion failed:\n" +
					"${subMessageIndent}value: ${value.toJson()}\n" +
					"${subMessageIndent}reason: $it"
			}
}
