package me.wietlol.unittest.core

import me.wietlol.unittest.core.models.TestModule
import me.wietlol.unittest.core.models.TestOptions

class HiddenTestModule : TestModule
{
	override val options = TestOptions(output = null)
}
