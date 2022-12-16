package me.wietlol.unittest.core.validators

import me.wietlol.unittest.core.models.TestOptions

class ContainsAllCollectionValidator<T>(
	override val options: TestOptions,
	val needles: List<T>,
) : Validator<Collection<T>>, ValidatorHelper
{
	override fun validate(value: Collection<T>): Validation
	{
		val set = value.toHashSet()
		val missingValues = needles
			.filter { set.contains(it).not() }
		
		val isValid = missingValues.isEmpty()
		val message = generateMessage(isValid, missingValues, needles)
		return Validation(isValid, message)
	}
	
	private fun generateMessage(isValid: Boolean, missingValues: List<T>, needles: Collection<T>): String =
		if (isValid)
			"$messageIndent'containsAll(needles=${needles.toDisplayString()})' assertion succeeded"
		else
			"$messageIndent'containsAll(needles=${needles.toDisplayString()})' assertion failed:\n" +
				"${subMessageIndent}needles: ${needles.toDisplayString()}\n" +
				"${subMessageIndent}missing: ${missingValues.toDisplayString()}"
}
