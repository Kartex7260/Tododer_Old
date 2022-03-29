package kartex.tododer.ui

import android.content.Context
import android.util.AttributeSet
import android.view.ViewGroup
import kartex.tododer.R
import kartex.tododer.lib.extensions.computeProgress
import kartex.tododer.lib.todo.IPlan
import kartex.tododer.lib.todo.ITask

class PlanDetailView : TreeTodoDetailView<ITask, IPlan> {

	// <editor-fold desc="FIELD`S">
	private lateinit var _progress: ProgressView
	// </editor-fold>

	// <editor-fold desc="PROP`S">
	override val xmlLayoutId: Int
		get() = R.layout.detail_card_plan
	// </editor-fold>

	// <editor-fold desc="CTOR`S">
	constructor(context: Context) : super(context)

	constructor(context: Context, attr: AttributeSet?) : super(context, attr)

	constructor(context: Context, attr: AttributeSet?, defStyleAttr: Int) : super(context, attr, defStyleAttr)
	// </editor-fold>

	override fun initViews(layout: ViewGroup) {
		super.initViews(layout)

		_progress = layout.findViewById(R.id.detailPlanProgress)
	}

	override fun onReadFromBind(todo: IPlan) {
		super.onReadFromBind(todo)

		_progress.ratio = todo.computeProgress()
	}
}