package kartex.tododer.lib.extensions

import kartex.tododer.lib.todo.IPlan
import kartex.tododer.lib.todo.ITask
import kartex.tododer.lib.todo.Plan
import kartex.tododer.lib.todo.Task

fun ITask.cloneWithoutChildren() = Task(id, parent).also {
	it.title = title
	it.time = time

	it.remark = remark
	it.check = check
}

fun IPlan.cloneWithoutChildren() = Plan(id, parent).also {
	it.title = title
	it.time = time

	it.remark = remark
}