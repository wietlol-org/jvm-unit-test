package unittest.core.validators

import unittest.core.models.TestOptions

class ComparableEqualToValidator<T : Comparable<T>>(
	override val options: TestOptions,
	val base: T,
	val comparator: Comparator<T> = Comparator.naturalOrder(),
) : Validator<T>, ValidatorHelper
{
	override fun validate(value: T): Validation
	{
		val isValid = comparator.compare(value, base) == 0
		val message = generateMessage(value, isValid)
		return Validation(isValid, message)
	}
	
	private fun generateMessage(value: T, isValid: Boolean): String =
		if (isValid)
			"$messageIndent'isEqualTo(base=${base.toJson()})' assertion succeeded"
		else
		{
			val valueDisplay = value.toJson()
			val baseDisplay = base.toJson()
			
			"$messageIndent'isEqualTo(base=$baseDisplay)' assertion failed:\n" +
				"${subMessageIndent}value: $valueDisplay\n" +
				"${subMessageIndent}base:  $baseDisplay"
		}
}
