package me.wietlol.unittest.core.assertions

import me.wietlol.unittest.core.LocalTestModule
import org.junit.Test

class CollectionTests : LocalTestModule()
{
	
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
