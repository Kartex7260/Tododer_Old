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

	override var state: String
		get() = super.state
		set(value) {
			super.state = value

			for (keyState in states) {
				if (keyState.key == value) {
					val state = keyState.value
					state.detail.isVisible = true
					state.list.isVisible = true
				} else {
					val state = keyState.value
					state.detail.isVisible = false
					state.list.isVisible = false
				}
			}
		}

	companion object {

		const val PLAN = "plan"
		const val TASK = "task"
	}

}