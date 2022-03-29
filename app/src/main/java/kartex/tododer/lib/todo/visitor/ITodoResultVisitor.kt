package kartex.tododer.lib.todo.visitor

import kartex.tododer.lib.todo.IPlan
import kartex.tododer.lib.todo.ITask

interface ITodoResultVisitor<T> {

	fun visitPlan(plan: IPlan): T
	fun visitTask(task: ITask): T
}