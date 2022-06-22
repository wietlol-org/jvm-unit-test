package me.wietlol.unittest.core.validators

import me.wietlol.unittest.core.models.TestOptions
import kotlin.reflect.KProperty

class PropertyConverter<T, R>(
	override val options: TestOptions,
	val mapping: T.() -> KProperty<R>,
) : Validator<T>, ValidatorHelper
{
	override fun validate(value: T): Validation
	{
		val property = value.mapping()
		val message = generateMessage(property)
		return Validation(true, message)
	}
	
	fun convert(value: T): R =
		value.mapping().getter.call()
	
	private fun generateMessage(property: KProperty<R>): String =
		"$messageIndent'property { ::${property.name} }' conversion succeeded"
}
