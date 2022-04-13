package kartex.tododer.ui

import android.content.Context
import android.util.AttributeSet
import android.view.View
import kartex.tododer.lib.model.IEventTodoDB
import kartex.tododer.lib.todo.ITask
import savvy.toolkit.Event

class TaskListView : TodoListView<ITask, IEventTodoDB<ITask>> {

	// <editor-fold desc="FIELD`S">
	private val eventLocker: Any = Any()
	// </editor-fold>

	// <editor-fold desc="CTOR`S">
	constructor(context: Context) : super(context)

	constructor(context: Context, attr: AttributeSet?) : super(context, attr)

	constructor(context: Context, attr: AttributeSet?, defStyleAttr: Int) : super(context, attr, defStyleAttr)
	// </editor-fold>

	// <editor-fold desc="PROP`S">
	val onCheckChange: Event<TaskView.Companion.CheckChangeEventArgs> = Event(eventLocker)
	// </editor-fold>

	override fun showTodo(todo: ITask, func: ((View) -> Unit)?) {
		super.showTodo(todo) {
			val view = it as TaskView
			view.onCheckChange += ::onCheckChangeProxy
		}
	}

	private fun onCheckChangeProxy(any: Any?, args: TaskView.Companion.CheckChangeEventArgs) {
		onCheckChange.invoke(eventLocker, args)
	}
}