package kartex.tododer.lib.todo

import android.content.Context
import android.view.View
import kartex.tododer.lib.todo.visitor.ITodoResultVisitor
import kartex.tododer.lib.todo.visitor.ITodoVisitor

open class Task(id: Int, parent: ITodo? = null) : TreeTodo<ITask>(id, parent), ITask, Iterable<ITodo> {

	override var check: Boolean = false

	override fun visit(visitor: ITodoVisitor) = visitor.visitTask(this)

	override fun <T> resultVisit(visitor: ITodoResultVisitor<T>) = visitor.visitTask(this)

	override fun clone() = Task(id, parent).also {
		it.title = title
		it.time = time

		it.remark = remark
		it.check = check

		for (task in todos) {
			val taskClone = task.clone()
			it.todos.add(taskClone)
		}
	}

	override fun toString(): String {
		return "${super.toString()} check: $check"
	}
}