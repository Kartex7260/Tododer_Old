package kartex.tododer.lib.todo

abstract class TreeTodo<Todo : ITodo>(id: Int, parent: ITodo? = null) : RemarkTodo(id, parent), ITreeTodo<Todo>, Iterable<ITodo> {
	override val todos: MutableList<Todo> = ArrayList()

	override fun toString(): String {
		return "${super.toString()}, tasks: ${todos.count()}"
	}

	override fun iterator(): Iterator<ITodo> = todos.iterator()
}