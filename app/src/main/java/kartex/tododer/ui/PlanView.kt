package kartex.tododer.ui

import android.content.Context
import android.util.AttributeSet
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.view.isVisible
import kartex.tododer.R
import kartex.tododer.lib.extensions.computeProgress
import kartex.tododer.lib.todo.IPlan

class PlanView : TodoView<IPlan> {

	// <editor-fold desc="FIELDS">
	// View объекты
	private lateinit var progressView: ProgressView
	private lateinit var textViewRemark: TextView
	// </editor-fold>

	// <editor-fold desc="PROP`S">
	override val xmlLayoutId: Int
		get() = R.layout.card_plan
	// </editor-fold>

	// <editor-fold desc="CTOR`S">
	constructor(context: Context) : super(context)

	constructor(context: Context, attr: AttributeSet?) : super(context, attr)

	constructor(context: Context, attr: AttributeSet?, defStyleAttr: Int) : super(context, attr, defStyleAttr)
	// </editor-fold>

	override fun initViews(layout: ViewGroup) {
		super.initViews(layout)

		progressView = layout.findViewById(R.id.planProgress)
		textViewRemark = layout.findViewById(R.id.planRemark)
	}

	override fun onReadFromBind(todo: IPlan) {
		super.onReadFromBind(todo)

		progressView.ratio = todo.computeProgress()

		if (todo.remark == "") {
			textViewRemark.isVisible = false
		} else {
			textViewRemark.isVisible = true
			textViewRemark.text = todo.remark
		}
	}
}