package kartex.tododer.lib.model

import kartex.tododer.lib.todo.ITodo
import savvy.toolkit.Event
import savvy.toolkit.EventArgs

class EventTodoDB<Todo : ITodo>(val db: ITodoDB<Todo>) : IEventTodoDB<Todo> {

	// <editor-fold desc="FIELD`S">
	private val eventLocker: Any = Any()
	// </editor-fold>

	// <editor-fold desc="PROP`S">
	override val onAdd: Event<TodoDBEventArgs<Todo>> = Event(eventLocker)
	override val onEdit: Event<TodoDBEventArgs<Todo>> = Event(eventLocker)
	override val onRemove: Event<TodoDBEventArgs<Todo>> = Event(eventLocker)
	override val onClear: Event<EventArgs> = Event(eventLocker)

	override val count: Int
		get() = db.count
	// </editor-fold>

	override fun iterator() = db.iterator()

	override fun add(t: Todo) {
		db.add(t)

		val eventArgs = TodoDBEventArgs(t)
		onAdd.invoke(eventLocker, eventArgs)
	}

	override fun get(func: (Todo) -> Boolean) = db.get(func)

	override fun remove(func: (Todo) -> Boolean): Todo? {
		val result = db.remove(func)
		if (result == null)
			return result

		val eventArgs = TodoDBEventArgs(result)
		onRemove.invoke(eventLocker, eventArgs)
		return result
	}

	override fun edit(id: Int, func: (Todo) -> Unit): Todo? {
		val result = db.edit(id, func)
		if (result == null)
			return result

		val eventArgs = TodoDBEventArgs(result)
		onEdit.invoke(eventLocker, eventArgs)
		return result
	}

	override fun get(id: Int) = db.get(id)

	override fun remove(id: Int): Todo? {
		val result = db.remove(id)
		if (result == null)
			return result

		val eventArgs = TodoDBEventArgs(result)
		onRemove.invoke(eventLocker, eventArgs)
		return result
	}

	override fun clear() {
		db.clear()

		val args = EventArgs()
		onClear.invoke(eventLocker, args)
	}
}