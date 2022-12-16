package me.wietlol.unittest.core.validators

import me.wietlol.unittest.core.models.TestOptions
import me.wietlol.utils.common.collections.BufferedSequence

class ContainsExactlySequenceValidator<T>(
	override val options: TestOptions,
	val needles: List<T>,
) : Validator<BufferedSequence<T>>, ValidatorHelper
{
	override fun validate(value: BufferedSequence<T>): Validation
	{
		val set = HashSet<T>()
		
		val iterator = value.iterator()
		
		var count = 0
		var hasMore = false
		fun setContainsValue(value: T): Boolean
		{
			if (set.contains(value))
			{
				set.remove(value)
				return true
			}
			while (iterator.hasNext())
			{
				count++
				if (count > needles.size)
				{
					hasMore = true
					return false
				}
				
				val next = iterator.next()
				
				if (next == value)
					return true
				else
					set.add(next)
			}
			return false
		}
		
		val missingValues = needles
			.filter { setContainsValue(it).not() }
		
		val isValid = missingValues.isEmpty()
		val message = generateMessage(isValid, needles, missingValues, hasMore)
		return Validation(isValid, message)
	}
	
	private fun generateMessage(isValid: Boolean, needles: Collection<T>, missingValues: List<T>, hasMore: Boolean): String =
		if (isValid)
			"$messageIndent'containsExactly(needles=${needles.toDisplayString()})' assertion succeeded"
		else
			"$messageIndent'containsExactly(needles=${needles.toDisplayString()})' assertion failed:\n" +
				"${subMessageIndent}needles: ${needles.toDisplayString()}\n" +
				"${subMessageIndent}missing: ${missingValues.toDisplayString()}\n" +
				"${subMessageIndent}hasMore: $hasMore"
}
