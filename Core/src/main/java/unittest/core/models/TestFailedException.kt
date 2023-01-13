package unittest.core.models

import unittest.core.validators.Validation

class TestFailedException(
	val case: TestCase,
	val failedAssertionResults: List<Validation>
) : Exception("Test '${case.name}' failed.")
