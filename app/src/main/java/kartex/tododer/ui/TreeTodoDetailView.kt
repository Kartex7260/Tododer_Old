package kartex.tododer.ui

import android.content.Context
import android.content.res.TypedArray
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatImageButton
import kartex.tododer.R
import kartex.tododer.lib.Const
import kartex.tododer.lib.MainDIBind
import kartex.tododer.lib.todo.ITodo
import kartex.tododer.lib.todo.ITreeTodo
import kartex.tododer.ui.dialogs.SortDialogFragment
import kartex.tododer.ui.sort.TodoSort

open class TreeTodoDetailView<TreeType : ITodo, Todo : ITreeTodo<TreeType>> : RemarkTodoDetailView<Todo> {

	// <editor-fold desc="FILED`S">
	private lateinit var buttonSortType: AppCompatImageButton
	private lateinit var buttonSortReverse: AppCompatImageButton

	private var _onChangeSort: OnChangeSort? = null

	private var _Main_diBind: MainDIBind? = null
	// </editor-fold>

	// <editor-fold desc="PROP`S">
	open var sortFunc: (() -> Unit)? = null

	override val xmlLayoutId: Int
		get() = R.layout.detail_card_tree_todo

	override val xmlStyleable: IntArray
		get() = R.styleable.TreeTodoDetailView

	open var mainDiBind: MainDIBind?
		get() = _Main_diBind
		set(value) {
			_Main_diBind = value
			updateRevButton()
			updateRevButton()
		}

	open var sort: TodoSort?
		get() = _Main_diBind?.sort
		set(value) {
			if (value == null) {
				_Main_diBind = null
				return
			}
			_Main_diBind?.sort = value
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
			if (_Main_diBind == null)
				return@setOnClickListener
			_Main_diBind!!.sort = _Main_diBind!!.sort.asReverse()
			_onChangeSort?.invoke(it, _Main_diBind!!.sort)
			updateRevButton()
		}
	}

	fun callback(sortResult: SortDialogFragment.Companion.SortResult) {
		if (_Main_diBind == null)
			return
		val currentReverse = _Main_diBind!!.sort.reverse
		val todoSort = sortResult.toTodoSort(currentReverse) ?: return
		_Main_diBind!!.sort = todoSort
		updateSortButton()
		_onChangeSort?.invoke(this, todoSort)
	}

	// <editor-fold desc="PRIVATE">
	private fun updateRevButton() {
		if (_Main_diBind == null)
			return
		if (_Main_diBind!!.sort.reverse)
			buttonSortReverse.rotation = 180f
		else
			buttonSortReverse.rotation = 0f
	}

	private fun updateSortButton() {
		if (_Main_diBind == null)
			return
		buttonSortType.setImageResource(mainDiBind!!.sort.iconResId)
	}
	// </editor-fold>
}

typealias OnChangeSort = View.(TodoSort) -> Unit