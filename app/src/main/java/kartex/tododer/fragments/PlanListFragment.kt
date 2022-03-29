package kartex.tododer.fragments

import android.content.Context
import android.os.Bundle
import android.util.AttributeSet
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.fragment.app.Fragment
import kartex.tododer.R
import kartex.tododer.lib.Const
import kartex.tododer.lib.model.ITodoDB
import kartex.tododer.lib.todo.IPlan
import kartex.tododer.lib.todo.visitor.CardViewVisitor
import org.kodein.di.DIAware
import org.kodein.di.android.closestDI
import org.kodein.di.instance

class PlanListFragment : Fragment(R.layout.fragment_plan_list), DIAware {

	// <editor-fold desc="FIELD`S">
	override val di by closestDI { requireContext() }
	private val db: ITodoDB<IPlan> by instance(Const.DITags.DB_MAIN)
	private val layoutParams: ViewGroup.LayoutParams by instance(Const.DITags.LP_MAIN_CARD)

	private lateinit var _cardVisitor: CardViewVisitor
	private lateinit var _root: LinearLayout
	// </editor-fold>

	override fun onInflate(context: Context, attrs: AttributeSet, savedInstanceState: Bundle?) {
		super.onInflate(context, attrs, savedInstanceState)
		_cardVisitor = CardViewVisitor(context)

		viewPlans(db)
	}

	private fun viewPlans(db: Iterable<IPlan>) {
		for (plan in db) {
			val planView = plan.resultVisit(_cardVisitor)
			_root.addView(planView, layoutParams)
		}
	}
}