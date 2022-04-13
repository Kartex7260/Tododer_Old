package kartex.tododer.lib.model

open class DataBase<T> : IDataBase<T> {

	protected val cache: MutableList<T> = ArrayList()

	override val count: Int
		get() = cache.count()

	override fun iterator(): Iterator<T> {
		return cache.iterator()
	}

	override fun add(t: T) {
		cache.add(t)
	}

	override fun get(func: (T) -> Boolean): T? {
		return firstOrNull(func)
	}

	override fun remove(func: (T) -> Boolean): T? {
		var t: T? = null
		cache.removeIf {
			if (func(it)) {
				t = it
				return@removeIf true
			}
			false
		}
		return t
	}

	override fun clear() {
		cache.clear()
	}

}