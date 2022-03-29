package kartex.tododer.lib.model

interface IDataBase<T> : Iterable<T> {

	fun add(t: T)
	fun get(func: (T) -> Boolean): T?
	fun remove(func: (T) -> Boolean): T?
}