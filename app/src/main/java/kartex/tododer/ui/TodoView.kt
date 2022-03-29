package kartex.tododer.ui

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.util.AttributeSet
import android.view.*
import android.widget.TextView
import androidx.appcompat.widget.AppCompatImageButton
import androidx.cardview.widget.CardView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.getSystemService
import kartex.tododer.R
import kartex.tododer.lib.extensions.toDpiF
import kartex.tododer.lib.todo.ITodo
import java.lang.NullPointerException

open class TodoView<Todo : ITodo> : CardView {

	// <editor-fold desc="FIELDS">
	// Поля видимых значений
	private var _bindTodo: Todo? = null // Привязяный элемент

	// View объекты
	private lateinit var rootLayout: ConstraintLayout // главный макет
	private lateinit var textViewTitle: TextView // Заголовок
	private lateinit var _buttonMore: AppCompatImageButton // Кнопка контекстного меню
	// </editor-fold>

	// <editor-fold desc="PROP`S"
	protected open val xmlLayoutId: Int
		get() = R.layout.card_todo

	protected open val xmlMenuId: Int
		get() = R.menu.todo_menu

	var bindTodo: Todo?
		get() = _bindTodo
		set(value) {
			_bindTodo = value
			updateFromBind()
		}

	var title: String
		get() = _bindTodo?.title ?: textViewTitle.text.toString()
		set(value) {
			textViewTitle.text = value
			_bindTodo?.title = value
			invalidate()
		}

	val buttonMore: AppCompatImageButton
		get() = _buttonMore

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

	open fun updateFromBind() {
		updateFromTodo(_bindTodo)
		invalidate()
		requestLayout()
	}

	open fun bindContextMenu(activity: Activity) {
		_buttonMore.setOnClickListener {
			activity.openContextMenu(_buttonMore)
		}
	}

	protected open fun initViews(layout: ViewGroup) {
		textViewTitle = layout.findViewById(R.id.todoTitle)
		_buttonMore = layout.findViewById(R.id.todoMoreButton)
	}

	protected open fun onReadFromBind(todo: Todo) {}

	override fun onCreateContextMenu(menu: ContextMenu?) {
		super.onCreateContextMenu(menu)
		val menuInflater = MenuInflater(context)
		menuInflater.inflate(R.menu.todo_menu, menu)
	}


	// <editor-fold desc="PRIVATE">
	@SuppressLint("ResourceType", "UseCompatLoadingForDrawables")
	private fun init(attr: AttributeSet?, defStyleAttr: Int) {
		isClickable = true
		isFocusable = true
		elevation = ELEVATION_DPI.toDpiF(context)

		rootLayout = getLayout(xmlLayoutId)
		addView(rootLayout)
		initViews(rootLayout)
	}

	private fun updateFromTodo(todo: Todo?) {
		if (todo == null) return

		updateData(todo)
		onReadFromBind(todo)
	}

	private fun updateData(todo: Todo) {
		todo.apply {
			textViewTitle.text = title
		}
	}

	private fun getLayout(resId: Int): ConstraintLayout {
		val layoutInflater = context.getSystemService<LayoutInflater>() ?:
		throw NullPointerException("Layout error")
		return layoutInflater.inflate(resId, null) as ConstraintLayout
	}
	// </editor-fold>

	companion object {

		const val ELEVATION_DPI = 2

		const val TOP_MARGIN_DPI = 11
		const val LEFT_MARGIN_DPI = 15

		// <editor-fold desc="STATIC PRIVATE">
		// </editor-fold>
	}
}