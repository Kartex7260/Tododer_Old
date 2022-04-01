package kartex.tododer.ui.events

import kartex.tododer.lib.todo.ITodo
import kartex.tododer.ui.TodoView
import savvy.toolkit.EventArgs

class TodoViewOnClickEventArgs(val todo: ITodo, val view: TodoView<out ITodo>) : EventArgs()