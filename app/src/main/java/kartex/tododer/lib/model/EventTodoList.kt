package kartex.tododer.lib.model

import kartex.tododer.lib.todo.ITodo
import savvy.toolkit.Event
import savvy.toolkit.EventArgs

class EventTodoList<Todo : ITodo>(val list: MutableList<Todo>) : IEventTodoDB<Todo> {

	private val eventLocker: Any = Any()

	override val onAdd: Event<TodoDBEventArgs<Todo>> = Event(eventLocker)
	override val onEdit: Event<TodoDBEventArgs<Todo>> = Event(eventLocker)
	override val onRemove: Event<TodoDBEventArgs<Todo>> = Event(eventLocker)
	override val onClear: Event<EventArgs> = Event(eventLocker)

	override val count: Int
		get() = list.count()

	override fun iterator() = list.iterator()

	override fun add(t: Todo) {
		list.add(t)

		val eventArgs = TodoDBEventArgs<Todo>(t)
		onAdd.invoke(eventLocker, eventArgs)
	}

	override fun get(func: (Todo) -> Boolean) = list.firstOrNull(func)

	override fun remove(func: (Todo) -> Boolean): Todo? {
		var result: Todo? = null
		list.removeIf {
			if (func(it)) {
				result = it
				return@removeIf true
			}
			false
		}
		if (result == null)
			return result

		val eventArgs = TodoDBEventArgs<Todo>(result!!)
		onRemove.invoke(eventLocker, eventArgs)
		return result
	}

	override fun edit(id: Int, func: (Todo) -> Unit): Todo? {
		val found = get(id)
		if (found == null)
			return found

		func(found)
		val eventArgs = TodoDBEventArgs<Todo>(found)
		onEdit.invoke(eventLocker, eventArgs)
		return found
	}

	override fun get(id: Int) = list.find { it.id == id }

	override fun remove(id: Int) = remove { it.id == id }

	override fun clear() {
		list.clear()

		val args = EventArgs()
		onClear.invoke(eventLocker, args)
	}
}