package kartex.tododer.lib.model.lazy

import kartex.tododer.lib.todo.ITodo
import kartex.tododer.lib.todo.visitor.ITodoResultVisitor
import kartex.tododer.lib.todo.visitor.ITodoVisitor

abstract class LazyTodo<Todo : ITodo>(val dir: String, val todo: Todo) : ITodo {

	override val id: Int
		get() = todo.id

	override var parent: ITodo?
		get() = todo.parent
		set(value) {
			todo.parent = value
		}

	override var title: String
		get() = todo.title
		set(value) {
			todo.title = value
		}

	override var time: Long
		get() = todo.time
		set(value) {
			todo.time = value
		}

	override fun visit(visitor: ITodoVisitor) = todo.visit(visitor)

	override fun <T> resultVisit(visitor: ITodoResultVisitor<T>) = todo.resultVisit(visitor)

	override fun clone() = todo.clone()

	abstract fun toFull(): Todo
}