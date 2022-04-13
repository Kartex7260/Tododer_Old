package kartex.tododer.lib.model

import kartex.tododer.lib.extensions.contains
import kartex.tododer.lib.extensions.getGsonInstance
import java.nio.charset.Charset
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.StandardOpenOption
import kotlin.io.path.Path

class CuidsDB(private val dir: String) : IDataBase<String> {

	private val manifestPath: Path = Path(dir, manifestFileName)
	private val gson = getGsonInstance()
	private val cuids: IDataBase<String> = DataBase()

	override val count: Int
		get() = cuids.count

	fun read() {
		val buffer = Files.readAllBytes(manifestPath)
		val json = buffer.toString(Charset.defaultCharset())
		val dto = gson.fromJson(json, DTO::class.java)

		for (cuid in dto.cuids) {
			cuids.add(cuid)
		}
	}

	override fun add(t: String) {
		if (cuids.contains { it == t })
			return
		cuids.add(t)
		saveFile()
	}

	override fun get(func: (String) -> Boolean) = cuids.get(func)

	override fun remove(func: (String) -> Boolean): String? {
		val removeResult = cuids.remove(func)
		if (removeResult != null) saveFile()
		return removeResult
	}

	override fun clear() {
		cuids.clear()
		saveFile()
	}

	override fun iterator(): Iterator<String> {
		return cuids.iterator()
	}

	// <editor-fold desc="private"
	private fun saveFile() {
		val json = toJson()
		val buffer = json.toByteArray(Charset.defaultCharset())
		Files.write(manifestPath, buffer, StandardOpenOption.CREATE)
	}

	private fun toJson(): String {
		val dto = DTO(cuids)
		return gson.toJson(dto)
	}
	// </editor-fold>

	private class DTO(val cuids: Iterable<String>)

	companion object {
		const val manifestFileName: String = "manifest.json"
	}
}