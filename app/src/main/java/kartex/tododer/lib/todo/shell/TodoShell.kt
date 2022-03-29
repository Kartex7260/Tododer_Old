package kartex.tododer.lib.todo.shell

import kartex.tododer.lib.todo.ITodo
import kartex.tododer.lib.todo.visitor.ITodoResultVisitor
import kartex.tododer.lib.todo.visitor.ITodoVisitor

open class TodoShell<Todo : ITodo>(val todo: Todo) : ITodo {

	override val id: Int
		get() = todo.id
	override var parent: ITodo?
		get() = todo.parent
		set(value) {
			todo.parent = value
		}

	override var time: Long
		get() = todo.time
		set(value) {
			todo.time = value
		}
	override var title: String
		get() = todo.title
		set(value) {
			todo.title = value
		}

	override fun visit(visitor: ITodoVisitor) = todo.visit(visitor)

	override fun <T> resultVisit(visitor: ITodoResultVisitor<T>) = todo.resultVisit(visitor)

	override fun clone() = todo.clone()

	override fun equals(other: Any?) = todo == other

	override fun hashCode() = todo.hashCode()

	override fun toString() = todo.toString()
}