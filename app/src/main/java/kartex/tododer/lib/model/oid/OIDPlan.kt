package kartex.tododer.lib.model.oid

import kartex.tododer.lib.todo.IPlan
import kartex.tododer.lib.todo.ITask
import kartex.tododer.lib.todo.ITodo
import kartex.tododer.lib.todo.Progress
import kartex.tododer.lib.todo.visitor.ITodoResultVisitor
import kartex.tododer.lib.todo.visitor.ITodoVisitor

class OIDPlan(id: Int, parent: ITodo? = null) : OIDTreeTodo<ITask>(id, parent), IPlan {

	override val progress: Progress = Progress()

	override val plans: MutableList<IPlan> = ArrayList()

	override fun clone() = OIDPlan(id, parent).also {
		for (plan in plans) {
			val planClone = plan.clone()
			it.plans.add(planClone)
		}

		for (task in todos) {
			val taskClone = task.clone()
			it.todos.add(taskClone)
		}
	}

	override fun visit(visitor: ITodoVisitor) = visitor.visitPlan(this)
	override fun <T> resultVisit(visitor: ITodoResultVisitor<T>) = visitor.visitPlan(this)
}