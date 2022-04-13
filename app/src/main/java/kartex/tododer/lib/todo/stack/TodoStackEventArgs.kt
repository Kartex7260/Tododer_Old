package kartex.tododer.lib.todo.stack

import kartex.tododer.lib.todo.ITodo
import savvy.toolkit.EventArgs

class TodoStackEventArgs<Todo : ITodo>(val todo: Todo, val args: Array<Any>? = null) : EventArgs()