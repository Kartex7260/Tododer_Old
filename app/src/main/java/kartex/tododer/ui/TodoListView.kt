package kartex.tododer.ui

import android.content.Context
import android.util.AttributeSet
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.core.widget.NestedScrollView
import kartex.tododer.lib.Const
import kartex.tododer.lib.IBindable
import kartex.tododer.lib.model.IEventTodoDB
import kartex.tododer.lib.model.TodoDBEventArgs
import kartex.tododer.lib.todo.ITodo
import kartex.tododer.lib.todo.visitor.CardViewVisitor
import kartex.tododer.ui.events.TodoViewOnClickEventArgs
import org.kodein.di.DI
import org.kodein.di.DIAware
import org.kodein.di.android.closestDI
import org.kodein.di.instance
import savvy.toolkit.Event

open class TodoListView<Todo : ITodo, DB: IEventTodoDB<Todo>> : NestedScrollView, IBindable<DB>, DIAware {

	// <editor-fold desc="FIELD`S">
	override val di: DI by closestDI()
	private val todoViewLayoutParams: ViewGroup.LayoutParams by instance(Const.DITags.LP_MAIN_CARD)

	private lateinit var _root: LinearLayout
	private lateinit var _cardVisitor: CardViewVisitor

	private var _bind: DB? = null
	// </editor-fold>

	// <editor-fold desc="PROP`S">
	override var bind: DB?
		get() = _bind
		set(value) {
			_bind?.apply {
				onAdd -= ::onAdd
				onEdit -= ::onEdit
				onRemove -= ::onRemove
			}
			_bind = value
			updateDB(_bind)
		}

	val onClick: Event<TodoViewOnClickEventArgs> = Event(this)
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

	override fun onReadFromBind(t: DB) { }

	override fun onWriteToBind(t: DB) { }

	// <editor-fold desc="PROTECTED">
	protected open fun updateDB(db: DB?) {
		_root.removeAllViews()

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
		val view = _root.findViewById<TodoView<ITodo>>(todo.id)
		view.updateFromBind()
	}
	protected open fun onRemove(any: Any?, args: TodoDBEventArgs<Todo>) {
		val todo = args.todo
		val view = _root.findViewById<TodoView<ITodo>>(todo.id)
		_root.removeView(view)
	}
	// </editor-fold>

	// <editor-fold desc="PRIVATES">
	private fun init() {
		_cardVisitor = CardViewVisitor(context)
		_root = LinearLayout(context).apply {
			orientation = LinearLayout.VERTICAL
		}
		addView(_root)
	}

	private fun showDB(db: DB) {
		for (todo in db) {
			showTodo(todo)
		}
	}

	private fun showTodo(todo: ITodo) {
		val view = createView(todo)
		_root.addView(view, todoViewLayoutParams)
	}

	private fun onClickCard(todo: ITodo, view: TodoView<out ITodo>) {
		val eventArgs = TodoViewOnClickEventArgs(todo, view)
		onClick.invoke(this, eventArgs)
	}

	private fun createView(todo: ITodo): TodoView<out ITodo> {
		val view = todo.resultVisit(_cardVisitor)
		view.id = todo.id
		view.setOnClickListener {
			onClickCard(todo, view)
		}
		return view
	}
	// </editor-fold>
}