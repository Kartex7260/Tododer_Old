package kartex.tododer.lib.todo.shell

import kartex.tododer.lib.todo.IRemarkTodo

open class RemarkTodoShell<Todo : IRemarkTodo>(todo: Todo) : TodoShell<Todo>(todo), IRemarkTodo {

	override var remark: String
		get() = todo.remark
		set(value) {
			todo.remark = value
		}

	override fun clone() = todo.clone()
}