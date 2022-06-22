package me.wietlol.unittest.core.assertions

import me.wietlol.unittest.core.models.TestModule
import org.junit.Test

class CollectionAssertionTest : TestModule
{
	private val successValues = listOf(1, 2, 3)
	private val failureValues = listOf(1, -2, 3)
	
	@Test
	fun `assert that allMatch, on success, gives summarized results for the last element in the collection`()
	{
		val result = test {
			assertThat(successValues)
				.allMatch { it.isGreaterThan(0) }
		}
		
		assert {
			assertThat(result)
				.property { ::testResults }
				.map { it[1] }
				.property { ::message }
				.isEqualTo("validating value 3")
		}
	}
	
	@Test
	fun `assert that allMatch, on failure, gives summarized results for the last element that was processed`()
	{
		val result = test {
			assertThat(failureValues)
				.allMatch { it.isGreaterThan(0) }
		}
		
		assert {
			assertThat(result)
				.property { ::testResults }
				.map { it[1] }
				.property { ::message }
				.isEqualTo("validating value -2")
		}
	}
	
	@Test
	fun `assert that allMatch, on failure, gives a summary message after validating the failing elements`()
	{
		val result = test {
			assertThat(failureValues)
				.allMatch { it.isGreaterThan(0) }
		}
		
		assert {
			assertThat(result)
				.property { ::testResults }
				.map { it[3] }
				.property { ::message }
				.isEqualTo("'allMatch()' assertion failed for element at 'it[1]' (size: 3)")
		}
	}
	
	@Test
	fun `assert that allMatch, on success, gives a summary message`()
	{
		val result = test {
			assertThat(successValues)
				.allMatch { it.isGreaterThan(0) }
		}
		
		assert {
			assertThat(result)
				.property { ::testResults }
				.map { it[3] }
				.property { ::message }
				.isEqualTo("'allMatch()' assertion succeeded on all (3) elements")
		}
	}
	
	@Test
	fun `assert that allMatch, on failure, by unexpected exception, throws the exception`()
	{
		val result = test {
			assertThat(successValues)
				.allMatch { throw Exception("unexpected exception") }
		}
		
		assert {
			assertThat(result)
				.property { ::testResults }
				.map { it[2] }
				.property { ::message }
				.startsWith("Unexpected error occurred while testing: java.lang.Exception: unexpected exception")
		}
	}
	
	@Test
	fun `assert that allMatch, on success, only consumes the feed iterable once`()
	{
		var consumptions = 0
		val lazyValues = Iterable {
			iterator {
				consumptions++
				yield(1)
				yield(2)
				yield(3)
			}
		}
		
		val result = test {
			assertThat(lazyValues)
				.allMatch { it.isGreaterThan(0) }
		}
		
		assert {
			assertThat(consumptions)
				.isEqualTo(1)
			
			assertThat(result)
				.property { ::testResults }
				.map { it[0] }
				.property { ::message }
				.isEqualTo("validating value Iterable<Integer> { ... }")
		}
	}
	
	@Test
	fun `assert that assertThat provides correct information for iterables`()
	{
		class ProxyIterable<E>(
			val values: Iterable<E>
		) : Iterable<E> by values
		
		val numbers = ProxyIterable(listOf(1, 2, 3))
		
		val result = test {
			assertThat(numbers)
				.allMatch { it.isGreaterThan(0) }
		}
		
		assert {
			assertThat(result)
				.property { ::testResults }
				.map { it[0] }
				.property { ::message }
				.isEqualTo("validating value Iterable<E> { ... }")
		}
	}
	
	@Test
	fun `assert that assertThat provides correct information for sequences`()
	{
		val numbers = sequence {
			yield(1)
			yield(2)
			yield(3)
		}
		
		val result = test {
			assertThat(numbers)
				.allMatch { it.isGreaterThan(0) }
		}
		
		assert {
			assertThat(result)
				.property { ::testResults }
				.map { it[0] }
				.property { ::message }
				.isEqualTo("validating value Sequence<T> { ... }")
		}
	}
}
