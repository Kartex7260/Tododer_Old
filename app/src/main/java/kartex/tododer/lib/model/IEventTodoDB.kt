package kartex.tododer.lib.model

import kartex.tododer.lib.todo.ITodo
import savvy.toolkit.Event
import savvy.toolkit.EventArgs

interface IEventTodoDB<Todo : ITodo> : ITodoDB<Todo> {

	val onAdd: Event<TodoDBEventArgs<Todo>>
	val onEdit: Event<TodoDBEventArgs<Todo>>
	val onRemove: Event<TodoDBEventArgs<Todo>>
	val onClear: Event<EventArgs>
}