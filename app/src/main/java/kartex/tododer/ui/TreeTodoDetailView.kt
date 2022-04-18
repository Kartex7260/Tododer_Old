package kartex.tododer.ui

import android.annotation.SuppressLint
import android.content.Context
import android.content.res.TypedArray
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatImageButton
import kartex.tododer.R
import kartex.tododer.extensions.getColorPrimary
import kartex.tododer.lib.Const
import kartex.tododer.lib.MainDIBind
import kartex.tododer.lib.todo.ITodo
import kartex.tododer.lib.todo.ITreeTodo
import kartex.tododer.ui.dialogs.SortDialogFragment
import kartex.tododer.ui.sort.TodoSort
import kartex.tododer.ui.sort.getDrawable

open class TreeTodoDetailView<TreeType : ITodo, Todo : ITreeTodo<TreeType>> : RemarkTodoDetailView<Todo> {

	// <editor-fold desc="FILED`S">
	private lateinit var buttonSortType: AppCompatImageButton
	private lateinit var buttonSortReverse: AppCompatImageButton

	private var _onChangeSort: OnChangeSort? = null

	private var _mainDiBind: MainDIBind? = null

	private var colorPrimary: Int = 0
	// </editor-fold>

	// <editor-fold desc="PROP`S">
	open var sortFunc: (() -> SortDialogFragment)? = null

	override val xmlLayoutId: Int
		get() = R.layout.detail_card_tree_todo

	override val xmlStyleable: IntArray
		get() = R.styleable.TreeTodoDetailView

	open var mainDiBind: MainDIBind?
		get() = _mainDiBind
		set(value) {
			_mainDiBind = value
			updateSortButton()
			updateRevButton()
		}

	open var sort: TodoSort?
		get() = _mainDiBind?.sort
		set(value) {
			if (value == null) {
				_mainDiBind = null
				return
			}
			_mainDiBind?.sort = value
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

	@SuppressLint("UseCompatLoadingForDrawables")
	override fun initViews(layout: ViewGroup) {
		super.initViews(layout)

		colorPrimary = context.getColorPrimary()

		buttonSortType = layout.findViewById(R.id.detailTreeSortType)
		buttonSortReverse = layout.findViewById(R.id.detailTreeSortReverse)

		val reverseDrawable = context.getDrawable(R.drawable.sort_reverse_24)?.apply {
			setTint(colorPrimary)
		}
		buttonSortReverse.setImageDrawable(reverseDrawable)

		buttonSortType.setOnClickListener { view ->
			if (_mainDiBind == null)
				return@setOnClickListener
			val sortDialog = sortFunc?.invoke() ?: return@setOnClickListener
			sortDialog.setCallback {
				val reverse = _mainDiBind!!.sort.reverse
				_mainDiBind!!.sort = it.toTodoSort(reverse) ?: return@setCallback
				_onChangeSort?.invoke(view, _mainDiBind!!.sort)
				updateSortButton()
			}
		}

		buttonSortReverse.setOnClickListener {
			if (_mainDiBind == null)
				return@setOnClickListener
			_mainDiBind!!.sort = _mainDiBind!!.sort.asReverse()
			_onChangeSort?.invoke(it, _mainDiBind!!.sort)
			updateRevButton()
		}
	}

	fun callback(sortResult: SortDialogFragment.Companion.SortResult) {
		if (_mainDiBind == null)
			return
		val currentReverse = _mainDiBind!!.sort.reverse
		val todoSort = sortResult.toTodoSort(currentReverse) ?: return
		_mainDiBind!!.sort = todoSort
		updateSortButton()
		_onChangeSort?.invoke(this, todoSort)
	}

	// <editor-fold desc="PRIVATE">
	private fun updateRevButton() {
		if (_mainDiBind == null)
			return
		if (_mainDiBind!!.sort.reverse)
			buttonSortReverse.rotation = 180f
		else
			buttonSortReverse.rotation = 0f
	}

	private fun updateSortButton() {
		if (_mainDiBind == null)
			return
		val drawable = _mainDiBind!!.sort.getDrawable(context).apply {
			val colorPrimary = context.getColorPrimary()
			setTint(colorPrimary)
		}
		buttonSortType.setImageDrawable(drawable)
	}
	// </editor-fold>
}

typealias OnChangeSort = View.(TodoSort) -> Unit