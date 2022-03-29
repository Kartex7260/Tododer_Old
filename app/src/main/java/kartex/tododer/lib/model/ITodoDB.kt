package kartex.tododer.lib.model

import kartex.tododer.lib.todo.ITodo

interface ITodoDB<T : ITodo> : IDataBase<T>, Iterable<T> {

	fun edit(id: Int, func: (T) -> Unit): T?
	fun get(id: Int): T?
	fun remove(id: Int): T?
}