package me.wietlol.unittest.core.validators

import me.wietlol.unittest.core.models.TestOptions

class ContainsCollectionValidator<T>(
	override val options: TestOptions,
	val needle: T,
) : Validator<Collection<T>>, ValidatorHelper
{
	override fun validate(value: Collection<T>): Validation
	{
		val isValid = value.contains(needle)
		val message = generateMessage(isValid, value, needle)
		return Validation(isValid, message)
	}
	
	private fun generateMessage(isValid: Boolean, collection: Collection<*>, needle: T): String =
		if (isValid)
			"$messageIndent'contains(needle=${needle.toDisplayString()})' assertion succeeded"
		else
			"$messageIndent'contains(needle=${needle.toDisplayString()})' assertion failed:\n" +
				"${subMessageIndent}size:   ${collection.size}\n" +
				"${subMessageIndent}needle: ${needle.toDisplayString()}"
}

