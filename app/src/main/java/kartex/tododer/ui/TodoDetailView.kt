package kartex.tododer.ui

import android.content.Context
import android.content.res.TypedArray
import android.text.Editable
import android.text.TextWatcher
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.EditText
import android.widget.FrameLayout
import androidx.appcompat.widget.AppCompatImageButton
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.getSystemService
import kartex.tododer.R
import kartex.tododer.lib.todo.ITodo
import savvy.toolkit.EventArgs
import java.lang.NullPointerException

typealias OnDeleteListener <Todo> = (TodoDetailView.Companion.DeleteEventArgs<Todo>) -> Unit

open class TodoDetailView<Todo : ITodo> : FrameLayout {

	// <editor-fold desc="FIELDS">
	private val textWatcher = object : TextWatcher {
		override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
		override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
		override fun afterTextChanged(s: Editable?) {
			if (autoWriteToBind && writeFromUser) {
				writeToBind()
			}
		}
	}

	// Поля видимых значений
	private var _bindTodo: Todo? = null // Привязяный элемент

	// View объекты
	private lateinit var layout: ConstraintLayout
	private lateinit var editTextTitle: EditText
	private lateinit var deleteButton: AppCompatImageButton

	private var writeFromUser: Boolean = true

	private var _deleteListener: OnDeleteListener<Todo>? = null
	// </editor-fold>

	// <editor-fold desc="PROP`S">
	open val xmlLayoutId: Int
		get() = R.layout.detail_card_todo

	open val xmlStyleable: IntArray
		get() = R.styleable.TodoDetailView

	open var bindTodo: Todo?
		get() = _bindTodo
		set(value) {
			_bindTodo = value
			readFromBind()
		}

	open var title: String
		get() {
			_bindTodo?.run {
				return title
			}
			return editTextTitle.text.toString()
		}
		set(value) {
			_bindTodo?.apply {
				title = value
			}
			editTextTitle.text = Editable.Factory.getInstance().newEditable(value)
		}

	open var autoWriteToBind: Boolean = false

	open var deleteListener: OnDeleteListener<Todo>?
		get() = _deleteListener
		set(value) {
			_deleteListener = value
			deleteButton.setOnClickListener {
				val args = DeleteEventArgs(_bindTodo)
				_deleteListener?.invoke(args)
			}
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

	open fun writeToBind() {
		_bindTodo?.also {
			onWriteToBind(it)
		}
	}

	open fun readFromBind() {
		_bindTodo?.also {
			troughEditTextListener {
				onReadFromBind(it)
			}
		}
		invalidate()
		requestLayout()
	}

	// <editor-fold desc="PROTECTED">
	// CALLBACK`S
	protected open fun onReadFromBind(todo: Todo) {
		editTextTitle.text = toEditable(todo.title)
	}

	protected open fun onWriteToBind(todo: Todo) {
		val title = editTextTitle.text.toString()
		todo.title = title
	}

	// INIT`S
	protected open fun obtainStyles(typedArray: TypedArray) {
		title = typedArray.getString(R.styleable.TodoDetailView_title) ?: ""
	}

	protected open fun initViews(layout: ViewGroup) {
		editTextTitle = layout.findViewById(R.id.detailTodoTitle)
		deleteButton = layout.findViewById(R.id.detailTodoDelete)

		registerEditTextAWTB(editTextTitle)
	}

	// UTIL
	protected open fun registerEditTextAWTB(editText: EditText) {
		editText.addTextChangedListener(textWatcher)
	}

	protected open fun toEditable(source: CharSequence): Editable {
		return Editable.Factory.getInstance().newEditable(source)
	}

	protected open fun troughEditTextListener(func: () -> Unit) {
		writeFromUser = false
		func()
		writeFromUser = true
	}
	// </editor-fold>

	// <editor-fold desc="PRIVATE">
	private fun init(attr: AttributeSet?, defStyleAttr: Int) {
		layout = getLayout(xmlLayoutId)
		addView(layout)
		initViews(layout)

		context.theme.obtainStyledAttributes(attr, xmlStyleable, defStyleAttr, 0).apply {
			try {
				obtainStyles(this)
			} finally {
				recycle()
			}
		}
	}

	private fun getLayout(resId: Int): ConstraintLayout {
		val layoutInflater = context.getSystemService<LayoutInflater>() ?:
		throw NullPointerException("Layout error")
		return layoutInflater.inflate(resId, null) as ConstraintLayout
	}
	// </editor-fold>

	companion object {

		class DeleteEventArgs<Todo : ITodo>(val bind: Todo?) : EventArgs()
	}
}