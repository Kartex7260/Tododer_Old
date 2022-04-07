package kartex.tododer.lib.todo.visitor

import android.view.View
import kartex.tododer.lib.extensions.getCUID
import kartex.tododer.lib.todo.IPlan
import kartex.tododer.lib.todo.ITask
import kartex.tododer.lib.todo.ITodo

open class ViewManagerVisitor(
	var viewVisitor: ITodoResultVisitor<out View>
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
		return cachedViews[cuid] ?: return createView(cuid, todo)
	}

	private fun createView(cuid: String, todo: ITodo): View = todo.resultVisit(viewVisitor).also {
		cachedViews[cuid] = it
	}
	// </editor-fold>
}