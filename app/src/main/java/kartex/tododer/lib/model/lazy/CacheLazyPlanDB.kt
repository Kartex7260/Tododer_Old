package kartex.tododer.lib.model.lazy

import kartex.tododer.lib.extensions.contains
import kartex.tododer.lib.model.DataBase
import kartex.tododer.lib.model.ITodoDB

open class CacheLazyPlanDB : ITodoDB<LazyPlan> {

	protected val cache: DataBase<LazyPlan> = DataBase()

	override val count: Int
		get() = cache.count

	override fun iterator() = cache.iterator()

	override fun add(t: LazyPlan) {
		if (cache.contains { it.id == t.id })
			return
		cache.add(t)
	}

	override fun get(func: (LazyPlan) -> Boolean) = cache.firstOrNull(func)

	override fun remove(func: (LazyPlan) -> Boolean) = cache.remove(func)

	override fun edit(id: Int, func: (LazyPlan) -> Unit): LazyPlan? {
		val lazyPlan = get(id)
		if (lazyPlan != null)
			func(lazyPlan)
		return lazyPlan
	}

	override fun get(id: Int) = get { it.id == id }

	override fun remove(id: Int) = remove { it.id == id }

	override fun clear() {
		cache.clear()
	}
}