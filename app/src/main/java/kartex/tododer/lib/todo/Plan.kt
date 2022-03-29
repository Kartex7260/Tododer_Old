package kartex.tododer.lib.todo

import kartex.tododer.lib.extensions.cloneTo
import kartex.tododer.lib.todo.visitor.ITodoResultVisitor
import kartex.tododer.lib.todo.visitor.ITodoVisitor

open class Plan(id: Int, parent: ITodo? = null) : TreeTodo<ITask>(id, parent), IPlan, Iterable<ITodo> {

	override val plans: MutableList<IPlan> = ArrayList()

	override val progress: Progress = Progress()

	override fun visit(visitor: ITodoVisitor) {
		visitor.visitPlan(this)
	}

	override fun <T> resultVisit(visitor: ITodoResultVisitor<T>) = visitor.visitPlan(this)

	override fun iterator(): Iterator<ITodo> {
		return Iterators(plans, todos)
	}

	override fun clone() = Plan(id, parent).also {
		it.title = title
		it.time = time

		it.remark = remark
		progress.cloneTo(it.progress)

		for (plan in plans) {
			val planClone = plan.clone()
			it.plans.add(planClone)
		}

		for (task in todos) {
			val taskClone = task.clone()
			it.todos.add(taskClone)
		}
	}


	private class Iterators(
			iterable1: Iterable<ITodo>,
			iterable2: Iterable<ITodo>
			) : Iterator<ITodo> {

		private val iterator1: Iterator<ITodo> = iterable1.iterator()
		private val iterator2: Iterator<ITodo> = iterable2.iterator()

		override fun hasNext(): Boolean {
			return if (iterator1.hasNext())
				true
			else
				iterator2.hasNext()
		}

		override fun next(): ITodo {
			return if (iterator1.hasNext())
				iterator1.next()
			else
				iterator2.next()
		}

	}

	private class EmptyPlan : Plan(0, null) {

		override val plans: MutableList<IPlan>
			get() = ArrayList()
		override var remark: String
			get() = "NON_PUBLIC_PLAN"
			set(_) {}
		override val progress: Progress
			get() = Progress()
		override val todos: MutableList<ITask>
			get() = ArrayList()
		override var title: String
			get() = "EMPTY"
			set(_) {}
		override var parent: ITodo?
			get() = null
			set(_) {}
		override var time: Long
			get() = 0
			set(_) {}
	}

	companion object {
		val empty: IPlan = EmptyPlan()
	}

}