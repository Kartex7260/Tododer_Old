package kartex.tododer.ui

import android.content.Context
import android.content.res.TypedArray
import android.util.AttributeSet
import android.view.ViewGroup
import android.widget.CheckBox
import kartex.tododer.R
import kartex.tododer.lib.todo.ITask
import kartex.tododer.ui.dialogs.SortDialogFragment
import kartex.tododer.ui.sort.SortByTime

class TaskDetailView : TreeTodoDetailView<ITask, ITask>  {

	// <editor-fold desc="FIELD`S">
	private lateinit var _check: CheckBox
	// </editor-fold>

	override val xmlLayoutId: Int
		get() = R.layout.detail_card_task
	// </editor-fold>

	// <editor-fold desc="CTOR`S">
	constructor(context: Context) : super(context)

	constructor(context: Context, attr: AttributeSet?) : super(context, attr)

	constructor(context: Context, attr: AttributeSet?, defStyleAttr: Int) : super(context, attr, defStyleAttr)
	// </editor-fold>

	override fun initViews(layout: ViewGroup) {
		super.initViews(layout)

		_check = layout.findViewById(R.id.detailTaskCheck)
		_check.setOnCheckedChangeListener { _, isChecked -> bindTodo?.check = isChecked }
	}

	override fun onWriteToBind(todo: ITask) {
		super.onWriteToBind(todo)

		todo.check = _check.isChecked
	}

	override fun onReadFromBind(todo: ITask) {
		super.onReadFromBind(todo)

		_check.isChecked = todo.check
	}
}