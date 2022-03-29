package kartex.tododer.lib.todo.shell

import kartex.tododer.lib.todo.IPlan
import kartex.tododer.lib.todo.ITask
import kartex.tododer.lib.todo.Progress

class PlanShell(todo: IPlan) : TreeTodoShell<ITask, IPlan>(todo), IPlan {

	override val plans: MutableList<IPlan>
		get() = todo.plans

	override val progress: Progress
		get() = todo.progress

	override fun clone() = todo.clone()
}