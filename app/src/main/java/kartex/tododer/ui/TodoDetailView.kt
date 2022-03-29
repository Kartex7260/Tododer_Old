package kartex.tododer.ui

import android.content.Context
import android.text.Editable
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.FrameLayout
import androidx.appcompat.widget.AppCompatImageButton
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.getSystemService
import kartex.tododer.R
import kartex.tododer.lib.todo.ITodo
import java.lang.NullPointerException

open class TodoDetailView<Todo : ITodo> : FrameLayout {

	// <editor-fold desc="FIELDS">
	// Поля видимых значений
	private var _bindTodo: Todo? = null // Привязяный элемент

	// View объекты
	private lateinit var layout: ConstraintLayout
	private lateinit var editTextTitle: EditText
	private lateinit var deleteButton: AppCompatImageButton
	// </editor-fold>

	// <editor-fold desc="PROP`S">
	open val xmlLayoutId: Int
		get() = R.layout.detail_card_todo

	open var bindTodo: Todo?
		get() = _bindTodo
		set(value) {
			_bindTodo = value
			updateFromBind()
		}
	// </editor-fold>

	// <editor-fold desc="CTOR`S">
	constructor(context: Context) : super(context) {
		init(null, 0)
	}

	constructor(context: Context, attr: AttributeSet?) : super(context, attr) {
		init(attr, 0)
	}

	constructor(context: Context, attr: AttributeSet?, defStyleAttr: Int) : super(context, attr, defStyleAttr) {
		init(attr, defStyleAttr)
	}
	// </editor-fold>

	open fun writeData() {
		val title = editTextTitle.text.toString()

		if (_bindTodo != null) {
			_bindTodo!!.title = title
			onWriteToBind(_bindTodo!!)
		}
	}

	open fun updateFromBind() {
		updateFromTodo(_bindTodo)
		invalidate()
		requestLayout()
	}

	open fun setOnDeleteListener(l: View.OnClickListener) {
		deleteButton.setOnClickListener(l)
	}

	protected open fun onReadFromBind(todo: Todo) {}

	protected open fun onWriteToBind(todo: Todo) {}

	protected open fun initViews(layout: ViewGroup) {
		editTextTitle = layout.findViewById(R.id.detailTodoTitle)
		deleteButton = layout.findViewById(R.id.detailTodoDelete)
	}

	// <editor-fold desc="PRIVATE">
	private fun init(attr: AttributeSet?, defStyleAttr: Int) {

		layout = getLayout(xmlLayoutId)
		addView(layout)
		initViews(layout)
	}

	private fun updateFromTodo(todo: Todo?) {
		if (todo == null) return

		updateData(todo)
		onReadFromBind(todo)
	}

	private fun updateData(todo: Todo) {
		todo.apply {
			editTextTitle.text = Editable.Factory.getInstance().newEditable(title)
		}
	}

	private fun getLayout(resId: Int): ConstraintLayout {
		val layoutInflater = context.getSystemService<LayoutInflater>() ?:
		throw NullPointerException("Layout error")
		return layoutInflater.inflate(resId, null) as ConstraintLayout
	}
	// </editor-fold>
}