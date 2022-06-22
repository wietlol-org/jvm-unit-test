package me.wietlol.unittest.core.validators

import me.wietlol.unittest.core.models.TestOptions

class MapForKeyConverter<K, V>(
	override val options: TestOptions,
	val key: K,
) : Validator<Map<K, V>>, ValidatorHelper
{
	override fun validate(value: Map<K, V>): Validation
	{
		val parsed = getValue(value)
		val message = generateMessage(parsed)
		return Validation(true, message)
	}
	
	fun getValue(value: Map<K, V>): V? =
		value[key]
	
	private fun generateMessage(value: V?): String =
		"$messageIndent'forKey(key=${key.toJson()})' conversion succeeded (value: ${value})"
}
