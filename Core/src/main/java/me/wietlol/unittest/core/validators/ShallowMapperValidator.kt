package me.wietlol.unittest.core.validators

class ShallowMapperValidator<I, O>(
	val validator: Validator<O>,
	val mapper: (I) -> O,
) : Validator<O>
{
	override fun validate(value: O): Validation =
		validator.validate(value)
}
