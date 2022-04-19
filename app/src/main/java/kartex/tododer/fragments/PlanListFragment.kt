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

	// State keys
	private val STATE_KEY_SCROLL: String = "scroll"
	// </editor-fold>

	fun resume() {
		bind?.apply {
			listView.updateAll()
		}
		setupActivityBind()
	}

	// <editor-fold desc="FRAGMENT API">
	// UP LIFECYCLE
	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setHasOptionsMenu(true)

		val mainActivity = requireActivity() as MainActivity
		activityBind = mainActivity.bind
		setupActivityBind()
	}

	override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
		bind = FragmentTodoListBinding.inflate(inflater, container, false)
		return bind?.root
	}

	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)

		bind?.apply {
			listView.bind = db
			listView.onClick += ::onClick
			listView.sortable = mainDiBind
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
			listView.bind = null
			listView.onClick -= ::onClick
			bind = null
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

	// <editor-fold desc="FRAGMENT NAVIGATION SETUP">
	private fun setupActivityBind() {
		activityBind?.apply {
			mainAddButton.setOnClickListener { addButtonClick(it) }
			mainDiBind.optionMenu?.apply {
				visiblePlanGroup(true)
				resume()
			}
			mainToolbar.setupToolbar()
		}
	}

	private fun androidx.appcompat.widget.Toolbar.setupToolbar() {
		setTitle(R.string.app_name)
		navigationIcon = null
		setNavigationOnClickListener {  }
	}

	private fun addButtonClick(view: View) {
		view.isEnabled = false
		val plan = db.createPlan()
		openPlan(plan)
		view.isEnabled = true
	}
	// </editor-fold>
	// </editor-fold>
}