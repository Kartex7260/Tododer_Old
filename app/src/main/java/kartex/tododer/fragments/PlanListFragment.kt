package kartex.tododer.fragments

import android.os.Bundle
import android.view.*
import androidx.core.widget.NestedScrollView
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import kartex.tododer.MainActivity
import kartex.tododer.R
import kartex.tododer.databinding.ActivityMainBinding
import kartex.tododer.databinding.FragmentTodoListBinding
import kartex.tododer.lib.Const
import kartex.tododer.lib.MainDIBind
import kartex.tododer.lib.extensions.createPlan
import kartex.tododer.lib.model.IEventTodoDB
import kartex.tododer.lib.todo.IPlan
import kartex.tododer.lib.todo.ITodo
import kartex.tododer.ui.events.TodoViewOnClickEventArgs
import org.kodein.di.DI
import org.kodein.di.DIAware
import org.kodein.di.android.closestDI
import org.kodein.di.instance

class PlanListFragment : Fragment(R.layout.fragment_todo_list), DIAware {

	// <editor-fold desc="FIELD`S">
	private var bind: FragmentTodoListBinding? = null
	private var activityBind: ActivityMainBinding? = null

	override val di: DI by closestDI { requireContext() }
	private val mainDiBind: MainDIBind by instance()
	private val db: IEventTodoDB<IPlan> by instance(Const.DITags.DB_MAIN)

	private var firstResume: Boolean = true

	// State keys
	private val STATE_KEY_SCROLL: String = "scroll"
	// </editor-fold>

	fun resume() {
		bind?.apply {
			fragmentTodoList.updateAll()
		}
		setupCurrentFragmentBindState()
	}

	// <editor-fold desc="FRAGMENT API">
	// UP LIFECYCLE
	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setHasOptionsMenu(true)

		val mainActivity = requireActivity() as MainActivity
		activityBind = mainActivity.bind
		setupCurrentFragmentBindState()
	}

	override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
		bind = FragmentTodoListBinding.inflate(inflater, container, false)
		return bind?.root
	}

	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)

		bind?.apply {
			fragmentTodoList.bind = db
			fragmentTodoList.onClick += ::onClick
		}

		savedInstanceState?.apply {
			val nestedScrollView = view as NestedScrollView
			nestedScrollView.scrollY = getInt(STATE_KEY_SCROLL)
		}
	}

	// DOWN LIFECYCLE
	override fun onDestroyView() {
		super.onDestroyView()
		bind?.apply {
			fragmentTodoList.bind = null
			fragmentTodoList.onClick -= ::onClick
			bind = null
		}
	}

	override fun onSaveInstanceState(outState: Bundle) {
		super.onSaveInstanceState(outState)
		val nestedScrollView = requireView() as NestedScrollView
		outState.putInt(STATE_KEY_SCROLL, nestedScrollView.scrollY)
	}

	override fun onOptionsItemSelected(item: MenuItem): Boolean {
		return super.onOptionsItemSelected(item)
	}
	// </editor-fold>

	// <editor-fold desc="PRIVATE">
	private fun onClick(any: Any?, args: TodoViewOnClickEventArgs) {
		val plan = args.todo as IPlan
		openPlan(plan)
	}

	private fun openPlan(plan: IPlan) {
		mainDiBind.plan = plan

		parentFragmentManager.commit {
			hide(this@PlanListFragment)
			setReorderingAllowed(true)
			add(R.id.mainFragmentContainer, DetailFragment::class.java, null, Const.FragmentTags.DETAIL)
			addToBackStack(Const.BACK_STACK_MAIN)
		}
	}

	// <editor-fold desc="BINDING FUNCTIONS">
	private fun setupCurrentFragmentBindState() {
		activityBind?.apply {
			mainAddButton.setOnClickListener { addButtonClick(it) }
			mainDiBind.optionMenu?.visiblePlanGroup(true)
		}
	}

	private fun addButtonClick(view: View) {
		val plan = db.createPlan()
		openPlan(plan)
	}
	// </editor-fold>
	// </editor-fold>
}