package kartex.tododer.lib.model

import kartex.tododer.lib.todo.ITodo
import savvy.toolkit.Event

class EventTodoDB<Todo : ITodo>(val db: ITodoDB<Todo>) : IEventTodoDB<Todo> {

	// <editor-fold desc="PROP`S">
	override val onAdd: Event<TodoDBEventArgs<Todo>> = Event(this)
	override val onEdit: Event<TodoDBEventArgs<Todo>> = Event(this)
	override val onRemove: Event<TodoDBEventArgs<Todo>> = Event(this)
	// </editor-fold>

	override fun iterator() = db.iterator()

	override fun add(t: Todo) {
		db.add(t)

		val eventArgs = TodoDBEventArgs<Todo>(t)
		onAdd.invoke(this, eventArgs)
	}

	override fun get(func: (Todo) -> Boolean) = db.get(func)

	override fun remove(func: (Todo) -> Boolean): Todo? {
		val result = db.remove(func)
		if (result == null)
			return result

		val eventArgs = TodoDBEventArgs<Todo>(result)
		onRemove.invoke(this, eventArgs)
		return result
	}

	override fun edit(id: Int, func: (Todo) -> Unit): Todo? {
		val result = db.edit(id, func)
		if (result == null)
			return result

		val eventArgs = TodoDBEventArgs<Todo>(result)
		onEdit.invoke(this, eventArgs)
		return result
	}

	override fun get(id: Int) = db.get(id)

	override fun remove(id: Int): Todo? {
		val result = db.remove(id)
		if (result == null)
			return result

		val eventArgs = TodoDBEventArgs<Todo>(result)
		onRemove.invoke(this, eventArgs)
		return result
	}

}