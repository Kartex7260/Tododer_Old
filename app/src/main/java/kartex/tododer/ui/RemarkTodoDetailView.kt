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

	override fun initViews(layout: ViewGroup) {
		super.initViews(layout)

		editTextRemark = layout.findViewById(R.id.detailRemark)
	}

	override fun onWriteToBind(todo: Todo) {
		todo.remark = editTextRemark.text.toString()
	}

	override fun onReadFromBind(todo: Todo) {
		editTextRemark.text = Editable.Factory.getInstance().newEditable(todo.remark)
	}
}