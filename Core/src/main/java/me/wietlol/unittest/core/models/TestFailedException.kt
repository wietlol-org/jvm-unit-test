package me.wietlol.unittest.core.models

import me.wietlol.unittest.core.models.TestCase
import me.wietlol.unittest.core.validators.Validation

class TestFailedException(
	val case: TestCase,
	val failedAssertionResults: List<Validation>
) : Exception("Test '${case.name}' failed.")
{
}
