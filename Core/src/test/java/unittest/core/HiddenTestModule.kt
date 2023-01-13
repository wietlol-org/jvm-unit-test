package unittest.core

import unittest.core.models.TestModule
import unittest.core.models.TestOptions

class HiddenTestModule : TestModule
{
	override val options = TestOptions(output = null)
}
