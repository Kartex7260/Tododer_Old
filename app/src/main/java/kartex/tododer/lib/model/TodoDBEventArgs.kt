package kartex.tododer.lib.model

import kartex.tododer.lib.todo.ITodo
import savvy.toolkit.EventArgs

class TodoDBEventArgs<Todo : ITodo>(val todo: ITodo) : EventArgs() {
}