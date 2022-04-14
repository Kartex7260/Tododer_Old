package kartex.tododer.lib.todo.visitor

import android.view.View
import kartex.tododer.lib.extensions.getCUID
import kartex.tododer.lib.todo.IPlan
import kartex.tododer.lib.todo.ITask
import kartex.tododer.lib.todo.ITodo
import kartex.tododer.ui.TodoView

open class ViewManagerVisitor(
	val viewVisitor: ITodoResultVisitor<out View>
) : ITodoResultVisitor<View> {

	private val cachedViews: MutableMap<String, View> = HashMap()

	open fun registry(todo: ITodo, view: View) {
		val cuid = todo.getCUID()
		if (cachedViews.containsKey(cuid))
			return

		cachedViews[cuid] = view
	}

	override fun visitPlan(plan: IPlan) = getOrCreate(plan)

	override fun visitTask(task: ITask) = getOrCreate(task)

	// <editor-fold desc="PRIVATE">
	private fun getOrCreate(todo: ITodo): View {
		val cuid = todo.getCUID()
		return cachedViews[cuid]?.also {
			val todoView = it as TodoView<ITodo>
			todoView.bindTodo = todo
		} ?: return createView(cuid, todo)
	}

	private fun createView(cuid: String, todo: ITodo): View {
		val view = todo.resultVisit(viewVisitor)
		cachedViews[cuid] = view
		return view
	}
	// </editor-fold>
}