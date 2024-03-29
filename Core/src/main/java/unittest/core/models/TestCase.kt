package unittest.core.models

import unittest.core.assertions.Assertion
import unittest.core.assertions.AssertionResult
import unittest.core.assertions.ValueAssertion
import unittest.core.validators.*
import utils.common.append
import utils.common.collections.BufferedSequence
import utils.common.collections.LazyList
import utils.common.collections.buffered
import java.math.BigDecimal
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import kotlin.reflect.KFunction
import kotlin.reflect.KProperty

@Suppress("unused", "MemberVisibilityCanBePrivate")
class TestCase(
	val name: String,
	override val options: TestOptions,
) : ValidatorHelper
{
	val results: MutableList<Validation> = mutableListOf()
	
	fun <T> assertThat(value: T, modifier: String? = null): Assertion<T> =
		ValueAssertion(value, this)
			.also { results.add(Validation(true, "validating ${modifier?.append(" ") ?: ""}value ${value.toDisplayString()}")) }
	
	inline fun <reified E : Throwable> assertThrows(noinline function: () -> Unit): AssertionResult<E> =
		assertThat(runCatching { function() }.exceptionOrNull())
			.assert(ThrowExceptionValidator<E>(options)) { it!! as E }
	
	fun <T, R> Assertion<T>.assert(validator: Validator<T>, mapper: (T) -> R): AssertionResult<R> =
		validate(validator, mapper)
			.also {
				results.add(it.result)
				if (it.result.isValid.not())
					throw TestFailedException(this@TestCase, results)
			}
	
	fun <T> Assertion<T>.assert(validator: Validator<T>): AssertionResult<T> =
		assert(validator) { it }
	
	fun <T> Assertion<T>.assert(name: String, validator: (T) -> Boolean): AssertionResult<T> =
		assert(GenericValidator(options, name, validator)) { it }
	
	fun <T, R> Assertion<T>.property(mapping: T.() -> KProperty<R>): AssertionResult<R>
	{
		val validator = PropertyConverter(options, mapping)
		return assert(validator, validator::convert)
	}
	
	fun <T, R> Assertion<T>.function(mapping: T.() -> KFunction<R>): AssertionResult<R>
	{
		val validator = FunctionConverter(options, mapping)
		return assert(validator, validator::convert)
	}
	
	inline fun <reified R> Assertion<Any>.isInstanceOf(): AssertionResult<R> =
		isInstanceOf(R::class.java)
	
	fun <R> Assertion<Any>.isInstanceOf(type: Class<R>): AssertionResult<R>
	{
		val validator = IsInstanceOfValidator(options, type)
		return assert(validator) { validator.cast(it).getOrThrow() }
	}
	
	fun <T> Assertion<T>.isEqualTo(base: T): AssertionResult<T> =
		assert(ObjectEqualValidator(options, base))
	
	fun <T> Assertion<T>.isNotEqualTo(base: T): AssertionResult<T> =
		assert(ObjectNotEqualValidator(options, base))
	
	fun <T> Assertion<T?>.isNull(): AssertionResult<Nothing?> =
		assert(NullValidator(options)) { null }
	
	fun <T> Assertion<T?>.isNotNull(): AssertionResult<T> =
		assert(NotNullValidator(options)) { it!! }
	
	fun <T : CharSequence> Assertion<T>.isEmpty(): AssertionResult<T> =
		assert(StringEmptyValidator(options))
	
	fun <T : CharSequence> Assertion<T>.isNotEmpty(): AssertionResult<T> =
		assert(StringNotEmptyValidator(options))
	
	fun <T : CharSequence> Assertion<T>.isBlank(): AssertionResult<T> =
		assert(StringBlankValidator(options))
	
	fun <T : CharSequence> Assertion<T>.isNotBlank(): AssertionResult<T> =
		assert(StringNotBlankValidator(options))
	
	fun <T : CharSequence> Assertion<T>.contains(
		search: CharSequence,
		ignoreCase: Boolean = false,
	): AssertionResult<T> =
		assert(StringContainsValidator(options, search, ignoreCase))
	
	fun <T : CharSequence> Assertion<T>.notContains(
		search: CharSequence,
		ignoreCase: Boolean = false,
	): AssertionResult<T> =
		assert(StringNotContainsValidator(options, search, ignoreCase))
	
	fun <T : CharSequence> Assertion<T>.startsWith(
		head: CharSequence,
		ignoreCase: Boolean = false
	): AssertionResult<T> =
		assert(StringStartsWithValidator(options, head, ignoreCase))
	
	fun <T : CharSequence> Assertion<T>.endsWith(
		tail: CharSequence,
		ignoreCase: Boolean = false,
	): AssertionResult<T> =
		assert(StringEndsWithValidator(options, tail, ignoreCase))
	
	fun <T : CharSequence> Assertion<T>.isEqualTo(
		base: CharSequence,
		ignoreCase: Boolean = false,
	): AssertionResult<T> =
		assert(StringEqualValidator(options, base, ignoreCase))
	
	fun <T : CharSequence> Assertion<T>.isNotEqualTo(
		base: CharSequence,
		ignoreCase: Boolean = false,
	): AssertionResult<T> =
		assert(StringNotEqualValidator(options, base, ignoreCase))
	
	fun <T : CharSequence> Assertion<T>.matches(regex: Regex, exactMatch: Boolean = true): AssertionResult<T> =
		assert(StringRegexValidator(options, regex, exactMatch))
	
	fun <T : CharSequence> Assertion<T>.toBoolean(): AssertionResult<Boolean>
	{
		val validator = ConvertToBooleanValidator(options)
		return assert(validator) { validator.parse(it).getOrThrow() }
	}
	
	fun <T : CharSequence> Assertion<T>.toInteger(): AssertionResult<Int>
	{
		val validator = ConvertToIntegerValidator(options)
		return assert(validator) { validator.parse(it).getOrThrow() }
	}
	
	fun <T : CharSequence> Assertion<T>.toDecimal(): AssertionResult<BigDecimal>
	{
		val validator = ConvertToBigDecimalValidator(options)
		return assert(validator) { validator.parse(it).getOrThrow() }
	}
	
	fun <T : CharSequence> Assertion<T>.toDate(): AssertionResult<LocalDate>
	{
		val validator = ConvertToDateValidator(options)
		return assert(validator) { validator.parse(it).getOrThrow() }
	}
	
	fun <T : CharSequence> Assertion<T>.toTime(): AssertionResult<LocalTime>
	{
		val validator = ConvertToTimeValidator(options)
		return assert(validator) { validator.parse(it).getOrThrow() }
	}
	
	fun <T : CharSequence> Assertion<T>.toDateTime(): AssertionResult<LocalDateTime>
	{
		val validator = ConvertToDateTimeValidator(options)
		return assert(validator) { validator.parse(it).getOrThrow() }
	}
	
	fun <T : Comparable<T>> Assertion<T>.isEqualToComparable(base: T): AssertionResult<T> =
		assert(ComparableEqualToValidator(options, base))
	
	fun <T : Comparable<T>> Assertion<T>.isNotEqualToComparable(base: T): AssertionResult<T> =
		assert(ComparableNotEqualToValidator(options, base))
	
	fun <T : Comparable<T>> Assertion<T>.isLessThan(base: T): AssertionResult<T> =
		assert(ComparableLessThanValidator(options, base))
	
	fun <T : Comparable<T>> Assertion<T>.isLessThanOrEqualTo(base: T): AssertionResult<T> =
		assert(ComparableLessThanOrEqualToValidator(options, base))
	
	fun <T : Comparable<T>> Assertion<T>.isGreaterThan(base: T): AssertionResult<T> =
		assert(ComparableGreaterThanValidator(options, base))
	
	fun <T : Comparable<T>> Assertion<T>.isGreaterThanOrEqualTo(base: T): AssertionResult<T> =
		assert(ComparableGreaterThanOrEqualToValidator(options, base))
	
	fun Assertion<BigDecimal>.isEqualTo(base: Int): AssertionResult<BigDecimal> =
		isEqualTo(base.toBigDecimal())
	
	fun Assertion<BigDecimal>.isEqualTo(base: Double): AssertionResult<BigDecimal> =
		isEqualTo(base.toBigDecimal())
	
	fun Assertion<BigDecimal>.isNotEqualTo(base: Int): AssertionResult<BigDecimal> =
		isNotEqualTo(base.toBigDecimal())
	
	fun Assertion<BigDecimal>.isNotEqualTo(base: Double): AssertionResult<BigDecimal> =
		isNotEqualTo(base.toBigDecimal())
	
	fun Assertion<BigDecimal>.isLessThan(base: Int): AssertionResult<BigDecimal> =
		isLessThan(base.toBigDecimal())
	
	fun Assertion<BigDecimal>.isLessThan(base: Double): AssertionResult<BigDecimal> =
		isLessThan(base.toBigDecimal())
	
	fun Assertion<BigDecimal>.isLessThanOrEqualTo(base: Int): AssertionResult<BigDecimal> =
		isLessThanOrEqualTo(base.toBigDecimal())
	
	fun Assertion<BigDecimal>.isLessThanOrEqualTo(base: Double): AssertionResult<BigDecimal> =
		isLessThanOrEqualTo(base.toBigDecimal())
	
	fun Assertion<BigDecimal>.isGreaterThan(base: Int): AssertionResult<BigDecimal> =
		isGreaterThan(base.toBigDecimal())
	
	fun Assertion<BigDecimal>.isGreaterThan(base: Double): AssertionResult<BigDecimal> =
		isGreaterThan(base.toBigDecimal())
	
	fun Assertion<BigDecimal>.isGreaterThanOrEqualTo(base: Int): AssertionResult<BigDecimal> =
		isGreaterThanOrEqualTo(base.toBigDecimal())
	
	fun Assertion<BigDecimal>.isGreaterThanOrEqualTo(base: Double): AssertionResult<BigDecimal> =
		isGreaterThanOrEqualTo(base.toBigDecimal())
	
	fun Assertion<Boolean>.isTrue(): AssertionResult<Boolean> =
		assert(BooleanEqualsValidator(options, true))
	
	fun Assertion<Boolean>.isFalse(): AssertionResult<Boolean> =
		assert(BooleanEqualsValidator(options, false))
	
	fun <T> Assertion<Iterable<T>>.allMatch(assertion: (Assertion<T>) -> AssertionResult<T>): AssertionResult<List<T>>
	{
		val match: Validator<Iterable<T>> = AllMatchValidator(options, assertion, this@TestCase)
//		val validator: Validator<Iterable<T>> = ShallowMapperValidator<Iterable<T>, List<T>>(match) { it.asList() }
		return take { it.asList() }.assert(match)

//		return assert(ShallowMapperValidator<Iterable<T>, List<T>>(AllMatchValidator<T>(options, assertion, this@TestCase)) { it.asList() })
	}
	
	@JvmName("allMatchSequence")
	fun <T> Assertion<Sequence<T>>.allMatch(assertion: (Assertion<T>) -> AssertionResult<T>): AssertionResult<List<T>>
	{
		val match: Validator<Iterable<T>> = AllMatchValidator(options, assertion, this@TestCase)
//		val validator: Validator<List<T>> = ShallowMapperValidator<Sequence<T>, List<T>>(match) { it.asList() }
		return take { it.asList() }.assert(match)

//		return assert(ShallowMapperValidator(AllMatchValidator(options, assertion, this@TestCase)) { LazyList(it) })
	}
	
	fun <T> Assertion<Iterable<T>>.anyMatch(assertion: (Assertion<T>) -> AssertionResult<T>): AssertionResult<List<T>>
	{
		val match: Validator<Iterable<T>> = AnyMatchValidator(options, assertion, this@TestCase)
		return take { it.asList() }.assert(match)
	}
	
	@JvmName("anyMatchSequence")
	fun <T> Assertion<Sequence<T>>.anyMatch(assertion: (Assertion<T>) -> AssertionResult<T>): AssertionResult<List<T>>
	{
		val match: Validator<Iterable<T>> = AnyMatchValidator(options, assertion, this@TestCase)
		return take { it.asList() }.assert(match)
	}
	
	fun <T> Assertion<Iterable<T>>.oneMatch(assertion: (Assertion<T>) -> AssertionResult<T>): AssertionResult<List<T>>
	{
		val match: Validator<Iterable<T>> = OneMatchValidator(options, assertion, this@TestCase)
		return take { it.asList() }.assert(match)
	}
	
	@JvmName("oneMatchSequence")
	fun <T> Assertion<Sequence<T>>.oneMatch(assertion: (Assertion<T>) -> AssertionResult<T>): AssertionResult<List<T>>
	{
		val match: Validator<Iterable<T>> = OneMatchValidator(options, assertion, this@TestCase)
		return take { it.asList() }.assert(match)
	}
	
	fun <T> Assertion<Iterable<T>>.noneMatch(assertion: (Assertion<T>) -> AssertionResult<T>): AssertionResult<List<T>>
	{
		val match: Validator<Iterable<T>> = NoneMatchValidator(options, assertion, this@TestCase)
		return take { it.asList() }.assert(match)
	}
	
	@JvmName("noneMatchSequence")
	fun <T> Assertion<Sequence<T>>.noneMatch(assertion: (Assertion<T>) -> AssertionResult<T>): AssertionResult<List<T>>
	{
		val match: Validator<Iterable<T>> = NoneMatchValidator(options, assertion, this@TestCase)
		return take { it.asList() }.assert(match)
	}
	
	@JvmName("isEmptyList")
	fun <T> Assertion<Iterable<T>>.isEmpty(): AssertionResult<List<T>> =
		take { it.asList() }
			.assert(EmptyListValidator(options, true))
	
	@JvmName("isEmptySequence")
	fun <T> Assertion<Sequence<T>>.isEmpty(): AssertionResult<BufferedSequence<T>> =
		take { it.buffered() }
			.assert(EmptySequenceValidator(options, true))
	
	@JvmName("isNotEmptyList")
	fun <T> Assertion<Iterable<T>>.isNotEmpty(): AssertionResult<List<T>> =
		take { it.asList() }
			.assert(EmptyListValidator(options, false))
	
	@JvmName("isNotEmptySequence")
	fun <T> Assertion<Sequence<T>>.isNotEmpty(): AssertionResult<BufferedSequence<T>> =
		take { it.buffered() }
			.assert(EmptySequenceValidator(options, false))
	
	fun <T> Assertion<Iterable<T>>.contains(needle: T): AssertionResult<List<T>> =
		take { it.asList() }
			.assert(ContainsCollectionValidator(options, needle))
	
	@JvmName("containsSequence")
	fun <T> Assertion<Sequence<T>>.contains(needle: T): AssertionResult<BufferedSequence<T>> =
		take { it.buffered() }
			.assert(ContainsSequenceValidator(options, needle))
	
	fun <T> Assertion<Iterable<T>>.containsAll(needles: Iterable<T>): AssertionResult<List<T>> =
		take { it.asList() }
			.assert(ContainsAllCollectionValidator(options, needles.toList()))
	
	fun <T> Assertion<Iterable<T>>.containsAll(vararg needles: T): AssertionResult<List<T>> =
		containsAll(needles.toList())
	
	@JvmName("containsAllSequence")
	fun <T> Assertion<Sequence<T>>.containsAll(needles: Iterable<T>): AssertionResult<BufferedSequence<T>> =
		take { it.buffered() }
			.assert(ContainsAllSequenceValidator(options, needles.toList()))
	
	@JvmName("containsAllSequence")
	fun <T> Assertion<Sequence<T>>.containsAll(vararg needles: T): AssertionResult<BufferedSequence<T>> =
		containsAll(needles.toList())
	
	fun <T> Assertion<Iterable<T>>.containsExactly(needles: Iterable<T>): AssertionResult<List<T>> =
		take { it.asList() }
			.assert(ContainsExactlyCollectionValidator(options, needles.toList()))
	
	fun <T> Assertion<Iterable<T>>.containsExactly(vararg needles: T): AssertionResult<List<T>> =
		containsExactly(needles.toList())
	
	@JvmName("containsExactlySequence")
	fun <T> Assertion<Sequence<T>>.containsExactly(needles: Iterable<T>): AssertionResult<BufferedSequence<T>> =
		take { it.buffered() }
			.assert(ContainsExactlySequenceValidator(options, needles.toList()))
	
	@JvmName("containsExactlySequence")
	fun <T> Assertion<Sequence<T>>.containsExactly(vararg needles: T): AssertionResult<BufferedSequence<T>> =
		containsExactly(needles.toList())
	
	fun <K, V> Assertion<Map<K, V>>.forKey(key: K): AssertionResult<V?>
	{
		val validator = MapForKeyConverter<K, V>(options, key)
		return assert(validator, validator::getValue)
	}
	
	private fun <E> Iterable<E>.asList(): List<E> =
		if (this is List<E>)
			this
		else
			LazyList(this)
	
	private fun <E> Sequence<E>.asList(): List<E> =
		LazyList(this)
}
