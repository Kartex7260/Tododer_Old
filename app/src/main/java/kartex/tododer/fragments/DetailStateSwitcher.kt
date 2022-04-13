package kartex.tododer.fragments

import androidx.core.view.isVisible
import kartex.tododer.lib.StateSwitcher
import kartex.tododer.lib.model.IEventTodoDB
import kartex.tododer.lib.todo.ITodo
import kartex.tododer.ui.TodoDetailView
import kartex.tododer.ui.TodoListView

class DetailStateSwitcher : StateSwitcher<DetailState>() {

	val detailView: TodoDetailView<out ITodo>?
		get() {
			val currentState = get() ?: return null
			return currentState.detail
		}

	val listView: TodoListView<out ITodo, out IEventTodoDB<out ITodo>>?
		get() {
			val currentState = get() ?: return null
			return currentState.list
		}

	companion object {

		const val PLAN = "plan"
		const val TASK = "task"
	}

}