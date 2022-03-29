package kartex.tododer.lib.todo.dto

open class TreeTodoDTO : RemarkTodoDTO(), ITodosParent {

	override var todosCUIDS: MutableList<String> = ArrayList()
}