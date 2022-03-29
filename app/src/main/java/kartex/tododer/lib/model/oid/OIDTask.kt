package kartex.tododer.lib.model.oid

import kartex.tododer.lib.todo.ITask
import kartex.tododer.lib.todo.ITodo
import kartex.tododer.lib.todo.visitor.ITodoResultVisitor
import kartex.tododer.lib.todo.visitor.ITodoVisitor

class OIDTask(id: Int, parent: ITodo? = null) : OIDTreeTodo<ITask>(id, parent), ITask {

	override var check: Boolean = false

	override fun clone() = OIDTask(id, parent).also {
		for (task in todos) {
			val taskClone = task.clone()
			it.todos.add(taskClone)
		}
	}

	override fun visit(visitor: ITodoVisitor) = visitor.visitTask(this)
	override fun <T> resultVisit(visitor: ITodoResultVisitor<T>) = visitor.visitTask(this)
}