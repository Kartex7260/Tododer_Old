package kartex.tododer.fragments

import kartex.tododer.lib.model.IEventTodoDB
import kartex.tododer.lib.todo.ITodo
import kartex.tododer.ui.TodoDetailView
import kartex.tododer.ui.TodoListView

class DetailState(
	val detail: TodoDetailView<out ITodo>,
	val list: TodoListView<out ITodo, out IEventTodoDB<out ITodo>>
)