package kartex.tododer.ui

import android.content.Context
import android.content.res.TypedArray
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatImageButton
import kartex.tododer.R
import kartex.tododer.lib.Const
import kartex.tododer.lib.DIProvider
import kartex.tododer.lib.todo.ITodo
import kartex.tododer.lib.todo.ITreeTodo
import kartex.tododer.ui.dialogs.SortDialogFragment
import kartex.tododer.ui.sort.SortByTime
import kartex.tododer.ui.sort.TodoSort
import org.kodein.di.DI
import org.kodein.di.DIAware
import org.kodein.di.android.closestDI
import org.kodein.di.instance
import kotlin.reflect.KProperty

open class TreeTodoDetailView<TreeType : ITodo, Todo : ITreeTodo<TreeType>> : RemarkTodoDetailView<Todo> {

	// <editor-fold desc="FILED`S">
	private lateinit var buttonSortType: AppCompatImageButton
	private lateinit var buttonSortReverse: AppCompatImageButton

	private var _onChangeSort: OnChangeSort? = null

	private var _diProvider: DIProvider? = null
	// </editor-fold>

	// <editor-fold desc="PROP`S">
	open var sortFunc: (() -> Unit)? = null

	override val xmlLayoutId: Int
		get() = R.layout.detail_card_tree_todo

	override val xmlStyleable: IntArray
		get() = R.styleable.TreeTodoDetailView

	open var diProvider: DIProvider?
		get() = _diProvider
		set(value) {
			_diProvider = value
			updateRevButton()
			updateRevButton()
		}

	open var sort: TodoSort?
		get() = _diProvider?.sort
		set(value) {
			if (value == null) {
				_diProvider = null
				return
			}
			_diProvider?.sort = value
			_onChangeSort?.invoke(this, value)
			updateSortButton()
			updateRevButton()
		}
	// </editor-fold>

	// <editor-fold desc="CTOR`S">
	constructor(context: Context) : super(context)

	constructor(context: Context, attr: AttributeSet?) : super(context, attr)

	constructor(context: Context, attr: AttributeSet?, defStyleAttr: Int) : super(context, attr, defStyleAttr)
	// </editor-fold>

	override fun obtainStyles(typedArray: TypedArray) {
		super.obtainStyles(typedArray)

		val standardSort = Const.SORT

		val sort = typedArray.getInt(R.styleable.TreeTodoDetailView_todoListFilter, standardSort.id)
		val sortReverse = typedArray.getBoolean(R.styleable.TreeTodoDetailView_sortReverse, standardSort.reverse)

		this.sort = SortDialogFragment.getFromParameters(sort, sortReverse) ?: this.sort

		updateSortButton()
		updateRevButton()
	}

	open fun setOnUpdateSortListener(l: OnChangeSort) {
		_onChangeSort = l
	}

	override fun initViews(layout: ViewGroup) {
		super.initViews(layout)

		buttonSortType = layout.findViewById(R.id.detailTreeSortType)
		buttonSortReverse = layout.findViewById(R.id.detailTreeSortReverse)

		buttonSortType.setOnClickListener {
			sortFunc?.invoke()
		}

		buttonSortReverse.setOnClickListener {
			if (_diProvider == null)
				return@setOnClickListener
			_diProvider!!.sort = _diProvider!!.sort.asReverse()
			_onChangeSort?.invoke(it, _diProvider!!.sort)
			updateRevButton()
		}
	}

	fun callback(sortResult: SortDialogFragment.Companion.SortResult) {
		if (_diProvider == null)
			return
		val currentReverse = _diProvider!!.sort.reverse
		val todoSort = sortResult.toTodoSort(currentReverse) ?: return
		_diProvider!!.sort = todoSort
		updateSortButton()
		_onChangeSort?.invoke(this, todoSort)
	}

	// <editor-fold desc="PRIVATE">
	private fun updateRevButton() {
		if (_diProvider == null)
			return
		if (_diProvider!!.sort.reverse)
			buttonSortReverse.rotation = 180f
		else
			buttonSortReverse.rotation = 0f
	}

	private fun updateSortButton() {
		if (_diProvider == null)
			return
		buttonSortType.setImageResource(diProvider!!.sort.iconResId)
	}
	// </editor-fold>
}

typealias OnChangeSort = View.(TodoSort) -> Unit