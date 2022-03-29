package kartex.tododer.lib.model

import kartex.tododer.lib.extensions.*
import kartex.tododer.lib.todo.IPlan

class PlanDB(private val dir: String) : CachePlanDB() {

	private val rootCuids: CuidsDB = CuidsDB(dir)

	fun read() {
		rootCuids.read()

		for (cuid in rootCuids) {
			val planDTO = readTodo(dir, cuid, ::jsonToPlanDTO)
			val plan = planDTO.toPlan()
			plan.fillPlans(dir, planDTO)
			cache.add(plan)
		}
	}

	override fun add(t: IPlan) {
		super.add(t)
		t.saveToDisk(dir)
	}

	override fun edit(id: Int, func: (IPlan) -> Unit) = super.edit(id, func).also { it?.saveToDisk(dir) }

	override fun remove(func: (IPlan) -> Boolean): IPlan? {
		val removeResult = super.remove(func)
		removeResult?.removeFromDisk(dir)
		return removeResult
	}
}