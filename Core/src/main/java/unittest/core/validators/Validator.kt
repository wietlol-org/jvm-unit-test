package unittest.core.validators

interface Validator<in T>
{
	fun validate(value: T): Validation
}
