package kartex.tododer.lib

import kartex.tododer.lib.todo.IPlan
import kartex.tododer.lib.todo.Plan
import kartex.tododer.ui.sort.SortByTime
import kartex.tododer.ui.sort.TodoSort
import savvy.toolkit.Event
import savvy.toolkit.EventArgs

class DIProvider {

	private var _sort: TodoSort = Const.SORT

	val onChangeSort: Event<DIProviderEventArgs<TodoSort>> = Event(this)

	var sort: TodoSort
		get() = _sort
		set(value) {
			_sort = value

			val args = DIProviderEventArgs<TodoSort>(value)
			onChangeSort.invoke(this, args)
		}


	var plan: IPlan = Plan.empty
}

class DIProviderEventArgs<T>(value: T) : EventArgs() {

}