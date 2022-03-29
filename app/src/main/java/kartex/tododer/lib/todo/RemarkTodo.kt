package kartex.tododer.lib.todo

abstract class RemarkTodo(id: Int, parent: ITodo? = null) : Todo(id, parent), IRemarkTodo {

	override var remark: String = ""
}