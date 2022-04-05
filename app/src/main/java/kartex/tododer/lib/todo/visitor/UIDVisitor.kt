package kartex.tododer.lib.todo.visitor

import kartex.tododer.lib.todo.IPlan
import kartex.tododer.lib.todo.ITask

class UIDVisitor : ITodoResultVisitor<String> {

	override fun visitPlan(plan: IPlan): String = "plan${plan.id}"

	override fun visitTask(task: ITask): String = "task${task.id}"
}