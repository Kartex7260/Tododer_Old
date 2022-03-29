package kartex.tododer.lib.todo.visitor

import kartex.tododer.lib.todo.IPlan
import kartex.tododer.lib.todo.ITask
import kartex.tododer.lib.todo.ITodo

interface ITodoVisitor {

	fun visitPlan(plan: IPlan)
	fun visitTask(task: ITask)
}

inline fun <reified T : ITodoVisitor> ITodo.visit(): T {
	val instance = T::class.java.newInstance()
	visit(instance)
	return instance
}