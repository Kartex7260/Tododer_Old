package kartex.tododer.ui

import android.content.Context
import android.util.AttributeSet
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.core.view.contains
import kartex.tododer.lib.Const
import kartex.tododer.lib.IBindable
import kartex.tododer.lib.extensions.removeFromParent
import kartex.tododer.lib.model.IEventTodoDB
import kartex.tododer.lib.model.TodoDBEventArgs
import kartex.tododer.lib.todo.ITodo
import kartex.tododer.lib.todo.visitor.CardViewVisitor
import kartex.tododer.lib.todo.visitor.ViewManagerVisitor
import kartex.tododer.lib.todo.visitor.getCardViewManager
import kartex.tododer.ui.events.TodoViewOnClickEventArgs
import org.kodein.di.DI
import org.kodein.di.DIAware
import org.kodein.di.android.closestDI
import org.kodein.di.instance
import savvy.toolkit.Event

open class TodoListView<Todo : ITodo, DB: IEventTodoDB<Todo>> : LinearLayout, IBindable<DB>, DIAware {

	// <editor-fold desc="FIELD`S">
	override val di: DI by closestDI()
	private val todoViewLayoutParams: ViewGroup.LayoutParams by instance(Const.DITags.LP_MAIN_CARD)

	private lateinit var _viewManager: ViewManagerVisitor

	private var _bind: DB? = null

	private val _eventLocker = Any()
	// </editor-fold>

	// <editor-fold desc="PROP`S">
	override var bind: DB?
		get() = _bind
		set(value) {
			if (_bind == value)
				return
			_bind?.apply {
				onAdd -= ::onAdd
				onEdit -= ::onEdit
				onRemove -= ::onRemove
			}
			_bind = value
			updateDB(_bind)
		}

	val onClick: Event<TodoViewOnClickEventArgs> = Event(_eventLocker)

	var onMenuDeleteClick: ((DB, Todo, MenuItem, TodoView<Todo>) -> Boolean)? = null
	// </editor-fold>

	// <editor-fold desc="CTOR`S">
	constructor(context: Context) : super(context) {
		init()
	}

	constructor(context: Context, attr: AttributeSet?) : super(context, attr) {
		init()
	}

	constructor(context: Context, attr: AttributeSet?, defStyleAttr: Int) : super(context, attr, defStyleAttr) {
		init()
	}
	// </editor-fold>

	open fun updateTodoCard(todo: ITodo) {
		val view = todo.resultVisit(_viewManager) as TodoView<*>
		view.updateFromBind()
	}

	open fun updateAll() {
		if (_bind == null) return

		for (todo in _bind!!) {
			val view = findViewById<TodoView<ITodo>>(todo.id)
			view.updateFromBind()
		}
	}

	override fun onReadFromBind(t: DB) { }

	override fun onWriteToBind(t: DB) { }

	// <editor-fold desc="PROTECTED">
	protected open fun showTodo(todo: Todo, func: ((View) -> Unit)? = null) {
		val view = createView(todo, func)
		view.removeFromParent()
		addView(view, todoViewLayoutParams)
	}

	protected open fun updateDB(db: DB?) {
		removeAllViews()

		if (db == null) return
		db.onAdd += ::onAdd
		db.onEdit += ::onEdit
		db.onRemove += ::onRemove
		showDB(db)
		onReadFromBind(db)
	}

	protected open fun onAdd(any: Any?, args: TodoDBEventArgs<Todo>) {
		val todo = args.todo
		showTodo(todo)
	}
	protected open fun onEdit(any: Any?, args: TodoDBEventArgs<Todo>) {
		val todo = args.todo
		val view = findViewById<TodoView<ITodo>>(todo.id)
		view.updateFromBind()
	}
	protected open fun onRemove(any: Any?, args: TodoDBEventArgs<Todo>) {
		val todo = args.todo
		val view = findViewById<TodoView<ITodo>>(todo.id)
		removeView(view)
	}
	// </editor-fold>

	// <editor-fold desc="PRIVATES">
	private fun init() {
		_viewManager = context.getCardViewManager()

		orientation = VERTICAL
	}

	private fun showDB(db: DB) {
		for (todo in db) {
			showTodo(todo)
		}
	}

	private fun onClickCard(todo: ITodo, view: View) {
		val eventArgs = TodoViewOnClickEventArgs(todo, view)
		onClick.invoke(_eventLocker, eventArgs)
	}

	private fun createView(todo: Todo, func: ((View) -> Unit)? = null): View {
		val view = todo.resultVisit(_viewManager) as TodoView<Todo>
		view.id = todo.id
		func?.invoke(view)
		view.setOnClickListener {
			onClickCard(todo, view)
		}
		view.onMenuDeleteClick = {
			_bind?.remove(todo.id)
			onMenuDeleteClick?.invoke(_bind!!, todo, it, view) ?: true
		}
		return view
	}
	// </editor-fold>
}