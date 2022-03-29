package kartex.tododer.lib.model.lazy

import kartex.tododer.lib.extensions.*
import kartex.tododer.lib.model.CuidsDB
import kartex.tododer.lib.todo.IPlan

class LazyPlanDB(private val dir: String) : CacheLazyPlanDB() {

	private val rootCuids: CuidsDB = CuidsDB(dir)

	fun read() {
		rootCuids.read()

		for (cuid in rootCuids) {
			val planDTO = readTodo(dir, cuid, ::jsonToPlanDTO)
			val plan = planDTO.toPlan()
			val lazyPlan = LazyPlan(dir, plan, planDTO)
			cache.add(lazyPlan)
		}
	}

	override fun add(t: LazyPlan) {
		super.add(t)
		t.saveToDisk(dir)
	}

	override fun edit(id: Int, func: (LazyPlan) -> Unit) = super.edit(id, func).also { it?.saveToDisk(dir) }

	override fun remove(func: (LazyPlan) -> Boolean): LazyPlan? {
		val removeResult = super.remove(func)
		if (removeResult != null) {
			val fullPlan = removeResult.toFullOID(dir)
			fullPlan.removeFromDisk(dir)
		}
		return removeResult
	}
}