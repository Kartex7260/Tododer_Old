package kartex.tododer.ui

import android.content.Context
import android.content.res.TypedArray
import android.text.Editable
import android.util.AttributeSet
import android.view.ViewGroup
import android.widget.EditText
import kartex.tododer.R
import kartex.tododer.lib.todo.IRemarkTodo

open class RemarkTodoDetailView<Todo : IRemarkTodo> : TodoDetailView<Todo> {

	// <editor-fold desc="FIELD`S">
	private lateinit var editTextRemark: EditText
	// </editor-fold>

	// <editor-fold desc="PROP`S">
	override val xmlLayoutId: Int
		get() = R.layout.detail_card_remark_todo

	override val xmlStyleable: IntArray
		get() = R.styleable.RemarkTodoDetailView

	open var remark: String
		get() {
			bindTodo?.run {
				return remark
			}
			return editTextRemark.text.toString()
		}
		set(value) {
			bindTodo?.apply {
				remark = value
			}
			editTextRemark.text = Editable.Factory.getInstance().newEditable(value)
		}
	// </editor-fold>

	// <editor-fold desc="CTOR`S">
	constructor(context: Context) : super(context)

	constructor(context: Context, attr: AttributeSet?) : super(context, attr)

	constructor(context: Context, attr: AttributeSet?, defStyleAttr: Int) : super(context, attr, defStyleAttr)
	// </editor-fold>

	// <editor-fold desc="RemarkTodoDetailView API">
	// INIT`S
	override fun obtainStyles(typedArray: TypedArray) {
		super.obtainStyles(typedArray)

		remark = typedArray.getString(R.styleable.RemarkTodoDetailView_remark) ?: ""
	}

	override fun initViews(layout: ViewGroup) {
		super.initViews(layout)

		editTextRemark = layout.findViewById(R.id.detailRemark)
		registerEditTextAWTB(editTextRemark)
	}

	// CALLBACKS`S
	override fun onReadFromBind(todo: Todo) {
		super.onReadFromBind(todo)
		editTextRemark.text = toEditable(todo.remark)
	}

	override fun onWriteToBind(todo: Todo) {
		super.onWriteToBind(todo)
		todo.remark = editTextRemark.text.toString()
	}
	// </editor-fold>
}