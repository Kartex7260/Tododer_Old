package kartex.tododer.lib.model

import kartex.tododer.lib.todo.IPlan

open class CachePlanDB : DataBase<IPlan>(), ITodoDB<IPlan> {

	override fun add(t: IPlan) {
		if (cache.contains(t))
			return
		cache.add(t)
	}

	override fun edit(id: Int, func: (IPlan) -> Unit): IPlan? {
		val plan = cache.firstOrNull { it.id == id } ?: return null
		func(plan)
		return plan
	}

	override fun get(id: Int) = get { it.id == id }

	override fun remove(id: Int) = remove { it.id == id }
}