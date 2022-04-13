package kartex.tododer.lib.model

interface IDataBase<T> : Iterable<T> {

	val count: Int

	fun add(t: T)
	fun get(func: (T) -> Boolean): T?
	fun remove(func: (T) -> Boolean): T?

	fun clear()
}