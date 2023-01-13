package unittest.core.validators

import unittest.core.models.TestOptions
import utils.common.collections.BufferedSequence

class ContainsAllSequenceValidator<T>(
	override val options: TestOptions,
	val needles: List<T>,
) : Validator<BufferedSequence<T>>, ValidatorHelper
{
	override fun validate(value: BufferedSequence<T>): Validation
	{
		val set = HashSet<T>()
		
		val iterator = value.iterator()
		
		fun setContainsValue(value: T): Boolean
		{
			if (set.contains(value))
				return true
			while (iterator.hasNext())
			{
				val next = iterator.next()
				set.add(next)
				
				if (next == value)
					return true
			}
			return false
		}
		
		val missingValues = needles
			.filter { setContainsValue(it).not() }
		
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
