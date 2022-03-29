package kartex.tododer.lib.todo.shell

import kartex.tododer.lib.todo.ITask

open class TaskShell(todo: ITask) : TreeTodoShell<ITask, ITask>(todo), ITask {

	override var check: Boolean
		get() = todo.check
		set(value) {
			todo.check = value
		}

	override fun clone() = todo.clone()
}