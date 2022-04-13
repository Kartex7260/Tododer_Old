package kartex.tododer.ui.events

import android.view.View
import kartex.tododer.lib.todo.ITodo
import savvy.toolkit.EventArgs

class TodoViewOnClickEventArgs(val todo: ITodo, val view: View) : EventArgs()