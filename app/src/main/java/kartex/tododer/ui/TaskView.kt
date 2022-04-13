package kartex.tododer.ui

import android.content.Context
import android.util.AttributeSet
import android.view.ViewGroup
import android.widget.CheckBox
import kartex.tododer.R
import kartex.tododer.lib.todo.ITask
import savvy.toolkit.Event
import savvy.toolkit.EventArgs

class TaskView : TodoView<ITask> {

	// <editor-fold desc="FIELDS"
	private val eventLocker: Any = Any()

	// View объект
	private lateinit var checkBox: CheckBox
	// </editor-fold>

	override val xmlLayoutId: Int
		get() = R.layout.card_task

	val onCheckChange: Event<CheckChangeEventArgs> = Event(eventLocker)

	constructor(context: Context) : super(context)

	constructor(context: Context, attr: AttributeSet?) : super(context, attr)

	constructor(context: Context, attr: AttributeSet?, defStyleAttr: Int) : super(context, attr, defStyleAttr)

	override fun initViews(layout: ViewGroup) {
		super.initViews(layout)

		checkBox = layout.findViewById(R.id.taskCheck)
		checkBox.setOnCheckedChangeListener { _, isChecked ->
			bindTodo?.check = isChecked

			val eventArgs = CheckChangeEventArgs(bindTodo, isChecked)
			onCheckChange.invoke(eventLocker, eventArgs)
		}
	}

	override fun onReadFromBind(todo: ITask) {
		checkBox.isChecked = todo.check
	}

	companion object {
		fun from(context: Context, task: ITask): TaskView {
			val taskView = TaskView(context)
			taskView.bindTodo = task
			return taskView
		}

		class CheckChangeEventArgs(val task: ITask?, val isChecked: Boolean) : EventArgs()
	}
}