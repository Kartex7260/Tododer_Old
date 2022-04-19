package kartex.tododer.lib.extensions

import kartex.tododer.lib.Const
import kartex.tododer.lib.TodoStackElement
import kartex.tododer.lib.todo.ITodo
import kartex.tododer.ui.sort.TodoSort

private val _sorts: MutableMap<String, TodoStackElement> = HashMap()

fun ITodo.getStackElement(): TodoStackElement {
	val cuid = getCUID()
	return _sorts[cuid]?.apply {
		todo = this@getStackElement
	} ?: TodoStackElement(this).apply {
		_sorts[cuid] = this
	}
}