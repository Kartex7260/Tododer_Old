package kartex.tododer.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.NestedScrollView
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import androidx.fragment.app.setFragmentResultListener
import kartex.tododer.R
import kartex.tododer.lib.Const
import kartex.tododer.lib.DIProvider
import kartex.tododer.lib.model.IEventTodoDB
import kartex.tododer.lib.todo.IPlan
import kartex.tododer.ui.TodoListView
import kartex.tododer.ui.events.TodoViewOnClickEventArgs
import org.kodein.di.DI
import org.kodein.di.DIAware
import org.kodein.di.android.closestDI
import org.kodein.di.instance

class PlanListFragment : Fragment(R.layout.fragment_todo_list), DIAware {

	// <editor-fold desc="FIELD`S">
	override val di: DI by closestDI { requireContext() }
	private val diProvider: DIProvider by instance()
	private val db: IEventTodoDB<IPlan> by instance(Const.DITags.DB_MAIN)

	private lateinit var _root: TodoListView<IPlan, IEventTodoDB<IPlan>>

	private var firstResume: Boolean = true

	// State keys
	private val STATE_KEY_SCROLL: String = "scroll"
	// </editor-fold>

	// <editor-fold desc="FRAGMENT API">
	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)

		_root = view.findViewById(R.id.fragmentTodoList)
		_root.bind = db
		_root.onClick += ::onClick

		savedInstanceState?.apply {
			val nestedScrollView = view as NestedScrollView
			nestedScrollView.scrollY = getInt(STATE_KEY_SCROLL)
		}
	}

	override fun onSaveInstanceState(outState: Bundle) {
		super.onSaveInstanceState(outState)
		val nestedScrollView = requireView() as NestedScrollView
		outState.putInt(STATE_KEY_SCROLL, nestedScrollView.scrollY)
	}
	// </editor-fold>

	// <editor-fold desc="PRIVATE">
	private fun onClick(any: Any?, args: TodoViewOnClickEventArgs) {
		val plan = args.todo as IPlan
		diProvider.plan = plan

		parentFragmentManager.commit {
			hide(this@PlanListFragment)
			setReorderingAllowed(true)
			add(R.id.mainFragmentContainer, DetailFragment::class.java, null, Const.FragmentTags.DETAIL)
			setFragmentResultListener(Const.ResultApiKeys.CLOSE_DETAIL) { _, _ ->
				_root.updateAll()
			}
			addToBackStack(Const.BACK_STACK_MAIN)
		}
	}
	// </editor-fold>
}