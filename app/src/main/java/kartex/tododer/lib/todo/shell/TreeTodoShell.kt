package kartex.tododer.lib.todo.shell

import kartex.tododer.lib.todo.ITask
import kartex.tododer.lib.todo.ITodo
import kartex.tododer.lib.todo.ITreeTodo

open class TreeTodoShell<TreeType : ITodo, Todo : ITreeTodo<TreeType>>(todo: Todo) : RemarkTodoShell<Todo>(todo), ITreeTodo<TreeType> {

	override val todos: MutableList<TreeType>
		get() = todo.todos

	override fun clone() = todo.clone()
}