package kartex.tododer.lib

import kartex.tododer.lib.todo.IPlan
import kartex.tododer.lib.todo.ITodo
import kartex.tododer.lib.todo.Plan
import kartex.tododer.lib.todo.stack.TodoStack
import kartex.tododer.ui.sort.SortByTime
import kartex.tododer.ui.sort.TodoSort
import savvy.toolkit.Event
import savvy.toolkit.EventArgs
import java.util.*

class DIProvider {

	private var _sort: TodoSort = Const.SORT

	// Events
	val onChangeSort: Event<DIProviderEventArgs<TodoSort>> = Event(this)

	// Variables
	var sort: TodoSort
		get() = _sort
		set(value) {
			_sort = value

			val args = DIProviderEventArgs<TodoSort>(value)
			onChangeSort.invoke(this, args)
		}
	var plan: IPlan = Plan.empty

	// Values
	val stack: TodoStack<ITodo> = TodoStack()
}

class DIProviderEventArgs<T>(val value: T) : EventArgs()