package kartex.tododer.lib

import kartex.tododer.lib.todo.IPlan
import kartex.tododer.lib.todo.ITodo
import kartex.tododer.lib.todo.Plan
import kartex.tododer.lib.todo.stack.TodoStack
import kartex.tododer.menus.MainOptionMenu
import kartex.tododer.ui.sort.TodoSort
import savvy.toolkit.Event
import savvy.toolkit.EventArgs

class MainDIBind {

	// Variables
	var plan: IPlan = Plan.empty
	var optionMenu: MainOptionMenu? = null

	// Values
	val stack: TodoStack<TodoStackElement> = TodoStack()
}

class ValueEventArgs<T>(val value: T) : EventArgs()

class TodoStackElement(var todo: ITodo, sort: TodoSort = Const.SORT) : ISortable {

	private val eventLocker: Any = Any()
	private var _sort: TodoSort = sort

	override val onChangeSort: Event<ValueEventArgs<TodoSort>> = Event(eventLocker)

	override var sort: TodoSort
		get() = _sort
		set(value) {
			_sort = value

			val args = ValueEventArgs<TodoSort>(_sort)
			onChangeSort.invoke(eventLocker, args)
		}

}