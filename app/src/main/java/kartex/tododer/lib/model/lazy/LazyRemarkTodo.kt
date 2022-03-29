package kartex.tododer.lib.model.lazy

import kartex.tododer.lib.todo.IRemarkTodo
import kartex.tododer.lib.todo.dto.RemarkTodoDTO

abstract class LazyRemarkTodo<Todo : IRemarkTodo>(dir: String, todo: Todo) :
	LazyTodo<Todo>(dir, todo), IRemarkTodo {

	override var remark: String
		get() = todo.remark
		set(value) {
			todo.remark = value
		}

	override fun clone() = todo.clone()
}