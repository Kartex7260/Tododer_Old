package kartex.tododer.ui.sort

import kartex.tododer.lib.todo.ITodo

abstract class TodoSort protected constructor(open val id: Int, open val iconResId: Int, open val reverse: Boolean = false) {

	open fun <T : ITodo> sort(iterable: Iterable<T>): List<T> {
		val sorted = sort0(iterable)
		if (reverse)
			return sorted.asReversed()
		return sorted
	}

	abstract fun asReverse(): TodoSort

	protected abstract fun <T : ITodo> sort0(iterable: Iterable<T>): List<T>
}