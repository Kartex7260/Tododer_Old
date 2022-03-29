package kartex.tododer.lib.todo

interface ITreeTodo<Todo : ITodo> : IRemarkTodo {

	val todos: MutableList<Todo>

	override fun clone(): ITreeTodo<Todo>
}