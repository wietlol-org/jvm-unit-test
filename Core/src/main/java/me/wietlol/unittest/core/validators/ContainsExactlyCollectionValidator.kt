package me.wietlol.unittest.core.validators

import me.wietlol.unittest.core.models.TestOptions

class ContainsExactlyCollectionValidator<T>(
	override val options: TestOptions,
	val needles: List<T>,
) : Validator<Collection<T>>, ValidatorHelper
{
	override fun validate(value: Collection<T>): Validation
	{
		val map = value
			.groupBy { it }
			.map { it.key to it.value.toMutableList() }
			.toMap(HashMap())
		val missingValues = needles
			.filter {
				val entry = map[it]
				if (!entry.isNullOrEmpty())
				{
					if (entry.size > 1)
						entry.removeLast()
					else
						map.remove(it)
					false
				}
				else
					true
			}
		val remaining = map.values.flatten()
		
		val isValid = missingValues.isEmpty() && remaining.isEmpty()
		val message = generateMessage(isValid, needles, missingValues, remaining)
		return Validation(isValid, message)
	}
	
	private fun generateMessage(isValid: Boolean, needles: Collection<T>, missingValues: List<T>, remaining: List<T>): String =
		if (isValid)
			"$messageIndent'containsExactly(needles=${needles.toDisplayString()})' assertion succeeded"
		else
			"$messageIndent'containsExactly(needles=${needles.toDisplayString()})' assertion failed:\n" +
				"${subMessageIndent}needles:   ${needles.toDisplayString()}\n" +
				"${subMessageIndent}missing:   ${missingValues.toDisplayString()}\n" +
				"${subMessageIndent}remaining: ${remaining.toDisplayString()}"
}
