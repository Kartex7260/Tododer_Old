package kartex.tododer.lib.extensions

import kartex.tododer.lib.model.EventTodoList
import kartex.tododer.lib.todo.IPlan
import kartex.tododer.lib.todo.ITask

fun <T> Iterable<T>.contains(func: (T) -> Boolean): Boolean {
	for (t in this) {
		if (func(t))
			return true
	}
	return false
}

fun <T> Array<T>.contains(func: (T) -> Boolean): Boolean {
	for (t in this) {
		if (func(t))
			return true
	}
	return false
}


private val planEventDBs: MutableMap<MutableList<ITask>, EventTodoList<ITask>> = HashMap()
private val taskEventDBs: MutableMap<MutableList<IPlan>, EventTodoList<IPlan>> = HashMap()

fun MutableList<ITask>.toTaskEventDB(): EventTodoList<ITask> {
	return planEventDBs[this] ?: EventTodoList(this).also {
		planEventDBs[this] = it
	}
}

fun MutableList<IPlan>.toPlanEventDB(): EventTodoList<IPlan> {
	return taskEventDBs[this] ?: EventTodoList(this).also {
		taskEventDBs[this] = it
	}
}