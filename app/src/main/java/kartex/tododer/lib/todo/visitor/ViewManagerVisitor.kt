package kartex.tododer.lib.todo.visitor

import android.view.View
import kartex.tododer.lib.extensions.getCUID
import kartex.tododer.lib.todo.IPlan
import kartex.tododer.lib.todo.ITask
import kartex.tododer.lib.todo.ITodo

open class ViewManagerVisitor(
	var viewVisitor: ITodoResultVisitor<out View>
) : ITodoResultVisitor<ViewManagerVisitor.Companion.ViewUnit> {

	private val cachedViews: MutableMap<String, ViewUnit> = HashMap()

	open fun registry(todo: ITodo, view: View) {
		val cuid = todo.getCUID()
		if (cachedViews.containsKey(cuid))
			return

		cachedViews[cuid] = ViewUnit(view).apply { notFirst() }
	}

	override fun visitPlan(plan: IPlan) = getOrCreate(plan)

	override fun visitTask(task: ITask) = getOrCreate(task)

	// <editor-fold desc="PRIVATE">
	private fun getOrCreate(todo: ITodo): ViewUnit {
		val cuid = todo.getCUID()
		return cachedViews[cuid].apply { this?.notFirst() } ?: return createView(cuid, todo)
	}

	private fun createView(cuid: String, todo: ITodo): ViewUnit {
		val view = todo.resultVisit(viewVisitor)
		return ViewUnit(view)
	}
	// </editor-fold>

	companion object {

		class ViewUnit(val view: View) {

			private var _first = true

			val isFirst: Boolean
				get() = _first

			fun notFirst() {
				_first = false
			}
		}
	}
}