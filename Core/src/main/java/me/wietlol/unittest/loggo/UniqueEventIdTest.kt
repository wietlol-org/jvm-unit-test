package me.wietlol.unittest.loggo

import me.wietlol.unittest.core.models.TestCase
import me.wietlol.utils.common.recursiveLet
import me.wietlol.utils.common.recursiveMapPreOrder
import java.io.File

// doesn't work 100% but is sufficient to catch most mistakes
fun TestCase.assertThatAllEventIdsAreUnique()
{
	val root = findRootFolder()
	val sourceFiles = sequenceOf(root)
		.recursiveMapPreOrder { it.listFiles()?.asSequence() ?: emptySequence() }
		.filter { it.isFile }
		.map { FileData.ofRelative(it, root) }
		.filter { it.path.contains("src") && it.path.endsWith(".kt") }
	
	val eventIds = sourceFiles
		.flatMap { extractEventIds(it) }
		.toList()
	
	val duplicateIds = eventIds
		.groupBy { it.id }
		.filter { it.value.size > 1 }
		.onEach {
			println("Duplicate ids in EventIds detected: (${it.key})")
			it.value.forEach { usage ->
				println("  - ${usage.file.path}: ${usage.line}")
			}
		}
		.toList()
	
	val duplicateNames = eventIds
		.groupBy { it.name }
		.filter { it.value.size > 1 }
		.onEach {
			println("Duplicate names in EventIds detected: (${it.key})")
			it.value.forEach { usage ->
				println("  - ${usage.file.path}: ${usage.line}")
			}
		}
		.toList()
	
	assertThat(duplicateIds)
		.assert("is empty") { it.isEmpty() }
	
	assertThat(duplicateNames)
		.assert("is empty") { it.isEmpty() }
}

/**
 * @return the highest folder containing a pom.xml
 */
private fun findRootFolder(): File =
	File("")
		.absoluteFile
		.recursiveLet { it.parentFile }
		.filter { it.isProjectFolder() }
		.lastOrNull()
		?: File("")

private fun File.isProjectFolder(): Boolean =
	resolve("pom.xml").exists()

private fun extractEventIds(fileData: FileData): Sequence<EventIdUsage>
{
	val eventIdVariableDeclarationPattern = Regex("(?<variable>\\w+)\\s*(?::\\s*EventId)?\\s*=\\s*EventId\\(\\s*(?<id>\\d+)\\s*,\\s*\"(?<name>[^\"]*)\"\\s*\\)")
	val eventIdPattern = Regex("logger.log[a-zA-Z]*\\(\\s*(?:EventId\\(\\s*(?<id>\\d+)\\s*,\\s*\"(?<name>[^\"]*)\"\\s*\\)|(?<variable>\\w+)\\s*,)")
	
	val variables = mutableMapOf<String, Pair<String, String>>()
	
	val lines = fileData.content.lineSequence()
	
	return lines
		.withIndex()
		.flatMap { (index, line) ->
			eventIdVariableDeclarationPattern
				.findAll(line)
				.forEach {
					variables[it.groups["variable"]!!.value] = it.groups["id"]!!.value to it.groups["name"]!!.value
				}
			
			eventIdPattern
				.findAll(line)
				.map { match ->
					val name = match.groups["variable"]?.value
					if (name != null)
					{
						val variable = variables[name]
						if (variable == null)
							null
						else
							EventIdUsage(
								fileData,
								index + 1,
								variable.first,
								variable.second
							)
					}
					else
					{
						EventIdUsage(
							fileData,
							index + 1,
							match.groups["id"]!!.value,
							match.groups["name"]!!.value
						)
					}
				}
		}
		.filterNotNull()
}

private class FileData(
	val path: String,
	val content: String,
)
{
	companion object
	{
		fun of(file: File) =
			FileData(file.path, file.readText())
		
		fun ofRelative(file: File, relativeTo: File) =
			FileData(file.relativeTo(relativeTo).path, file.readText())
	}
}

private data class EventIdUsage(
	val file: FileData,
	val line: Int,
	val id: String,
	val name: String,
)
