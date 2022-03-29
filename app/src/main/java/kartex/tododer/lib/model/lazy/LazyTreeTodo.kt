package kartex.tododer.lib.model.lazy

import kartex.tododer.lib.todo.ITodo
import kartex.tododer.lib.todo.ITreeTodo
import kartex.tododer.lib.todo.dto.TreeTodoDTO

abstract class LazyTreeTodo<TreeType : ITodo, Todo : ITreeTodo<TreeType>, DTO: TreeTodoDTO>(dir: String, todo: Todo, val dto: DTO) :
	LazyRemarkTodo<Todo>(dir, todo), ITreeTodo<TreeType> {

	override val todos: MutableList<TreeType>
		get() = todo.todos

	override fun clone() = todo.clone()
}