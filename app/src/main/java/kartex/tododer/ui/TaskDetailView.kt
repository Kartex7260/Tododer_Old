package kartex.tododer.ui

import android.content.Context
import android.content.res.TypedArray
import android.util.AttributeSet
import android.view.ViewGroup
import android.widget.CheckBox
import kartex.tododer.R
import kartex.tododer.lib.todo.ITask

open class TaskDetailView : TreeTodoDetailView<ITask, ITask>  {

	// <editor-fold desc="FIELD`S">
	private lateinit var _checkBox: CheckBox
	// </editor-fold>

	// <editor-fold desc="PROP`S">
	override val xmlLayoutId: Int
		get() = R.layout.detail_card_task

	override val xmlStyleable: IntArray
		get() = R.styleable.TaskDetailView

	open var check: Boolean
		get() {
			bindTodo?.run {
				return check
			}
			return _checkBox.isChecked
		}
		set(value) {
			bindTodo?.apply {
				check = value
			}
			_checkBox.isChecked = value
		}
	// </editor-fold>

	// <editor-fold desc="CTOR`S">
	constructor(context: Context) : super(context)

	constructor(context: Context, attr: AttributeSet?) : super(context, attr)

	constructor(context: Context, attr: AttributeSet?, defStyleAttr: Int) : super(context, attr, defStyleAttr)
	// </editor-fold>

	override fun obtainStyles(typedArray: TypedArray) {
		super.obtainStyles(typedArray)

		check = typedArray.getBoolean(R.styleable.TaskDetailView_check, check)
	}

	override fun initViews(layout: ViewGroup) {
		super.initViews(layout)

		_checkBox = layout.findViewById(R.id.detailTaskCheck)
		_checkBox.setOnCheckedChangeListener { _, isChecked -> bindTodo?.check = isChecked }
	}

	override fun onWriteToBind(todo: ITask) {
		super.onWriteToBind(todo)

		todo.check = _checkBox.isChecked
	}

	override fun onReadFromBind(todo: ITask) {
		super.onReadFromBind(todo)

		_checkBox.isChecked = todo.check
	}
}