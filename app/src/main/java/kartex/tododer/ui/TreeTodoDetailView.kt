package kartex.tododer.ui

import android.content.Context
import android.content.res.TypedArray
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatImageButton
import kartex.tododer.R
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

open class TreeTodoDetailView<TreeType : ITodo, Todo : ITreeTodo<TreeType>> : RemarkTodoDetailView<Todo>, DIAware {

	// <editor-fold desc="FILED`S">
	override val di: DI by closestDI()
	private val diProvider: DIProvider by instance()

	private lateinit var buttonSortType: AppCompatImageButton
	private lateinit var buttonSortReverse: AppCompatImageButton

	private var _onChangeSort: OnChangeSort? = null
	// </editor-fold>

	// <editor-fold desc="PROP`S">
	open var sortFunc: (() -> Unit)? = null

	override val xmlLayoutId: Int
		get() = R.layout.detail_card_tree_todo

	open var sort: TodoSort
		get() = diProvider.sort
		set(value) {
			diProvider.sort = value
			_onChangeSort?.invoke(this, value)
			updateSortButton()
			updateRevButton()
		}
	// </editor-fold>

	// <editor-fold desc="CTOR`S">
	constructor(context: Context) : super(context)

	constructor(context: Context, attr: AttributeSet?) : super(context, attr)

	constructor(context: Context, attr: AttributeSet?, defStyleAttr: Int) : super(context, attr, defStyleAttr)

	init {
		updateSortButton()
		updateRevButton()
	}
	// </editor-fold>

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
			diProvider.sort = diProvider.sort.asReverse()
			_onChangeSort?.invoke(it, diProvider.sort)
			updateRevButton()
		}
	}

	fun callback(sortResult: SortDialogFragment.Companion.SortResult) {
		val currentReverse = diProvider.sort.reverse
		val todoSort = sortResult.toTodoSort(currentReverse) ?: return
		diProvider.sort = todoSort
		updateSortButton()
		_onChangeSort?.invoke(this, todoSort)
	}

	// <editor-fold desc="PRIVATE">
	private fun updateRevButton() {
		if (diProvider.sort.reverse)
			buttonSortReverse.rotation = 180f
		else
			buttonSortReverse.rotation = 0f
	}

	private fun updateSortButton() {
		buttonSortType.setImageResource(diProvider.sort.iconResId)
	}
	// </editor-fold>
}

typealias OnChangeSort = View.(TodoSort) -> Unit