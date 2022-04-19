package kartex.tododer.fragments

import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.LinearLayout
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import kartex.tododer.MainActivity
import kartex.tododer.R
import kartex.tododer.databinding.ActivityMainBinding
import kartex.tododer.lib.Const
import kartex.tododer.lib.MainDIBind
import kartex.tododer.lib.StateSwitcher
import kartex.tododer.lib.TodoStackElement
import kartex.tododer.lib.extensions.*
import kartex.tododer.lib.model.IEventTodoDB
import kartex.tododer.lib.todo.*
import kartex.tododer.lib.todo.stack.TodoStack
import kartex.tododer.lib.todo.stack.TodoStackEventArgs
import kartex.tododer.ui.*
import kartex.tododer.ui.dialogs.SortDialogFragment
import kartex.tododer.ui.dialogs.TodoCreateDialogFragment
import kartex.tododer.ui.events.TodoViewOnClickEventArgs
import org.kodein.di.DI
import org.kodein.di.DIAware
import org.kodein.di.android.closestDI
import org.kodein.di.instance

class DetailFragment : Fragment(R.layout.fragment_todo_detail), DIAware {

	// <editor-fold desc="FIELD`S">
	override val di: DI by closestDI { requireContext() }
	private val mainDiBind: MainDIBind by instance()
	private var activityBind: ActivityMainBinding? = null

	private val states: DetailStateSwitcher = DetailStateSwitcher()

	private lateinit var stack: TodoStack<TodoStackElement>

	private var createTask: (() -> Task)? = null
	private var createPlan: (() -> Plan)? = null
	// </editor-fold>

	// <editor-fold desc="VIEW`S">
	private lateinit var _planDetail: PlanDetailView
	private lateinit var _taskDetail: TaskDetailView
	private lateinit var _rootLayout: LinearLayout

	private lateinit var _planList: TodoListView<IPlan, IEventTodoDB<IPlan>>
	private lateinit var _taskList: TaskListView

	private val _listLayoutParams = LinearLayout.LayoutParams(
		LinearLayout.LayoutParams.MATCH_PARENT,
		LinearLayout.LayoutParams.WRAP_CONTENT
	)
	// </editor-fold>

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		stack = mainDiBind.stack

		val mainActivity = requireActivity() as MainActivity
		activityBind = mainActivity.bind
		setupActivityBind()

		stack.onPush += ::onPush
		stack.onPop += ::onPop
	}

	override fun onDestroy() {
		super.onDestroy()
		activityBind = null

		stack.onPush -= ::onPush
		stack.onPop -= ::onPop
		stack.clear()

		val planListFragment = getPlanListFragment()
		planListFragment.resume()
	}

	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)
		val context = requireContext()

		val sortFun = fun (): SortDialogFragment {
			val sortDialog = SortDialogFragment()
			sortDialog.show(parentFragmentManager, null)
			return sortDialog
		}

		_planDetail = view.findViewById<PlanDetailView?>(R.id.fragmentDetailPlan).apply {
			autoWriteToBind = true
			sortFunc = sortFun
			deleteListener = fun (args: TodoDetailView.Companion.DeleteEventArgs<IPlan>) {
				if (args.bind == null)
					return
				onBackDelete(args.bind) { todo ->
					_planList.bind?.also { db ->
						db.remove(todo.id)
					}
				}
			}
		}
		_taskDetail = view.findViewById<TaskDetailView?>(R.id.fragmentDetailTask).apply {
			autoWriteToBind = true
			sortFunc = sortFun
			deleteListener = fun (args: TodoDetailView.Companion.DeleteEventArgs<ITask>) {
				if (args.bind == null)
					return
				onBackDelete(args.bind) { todo ->
					_taskList.bind?.also { db ->
						db.remove(todo.id)
					}
				}
			}
		}
		_rootLayout = view.findViewById(R.id.fragmentDetailRoot)

		_planList = TodoListView<IPlan, IEventTodoDB<IPlan>>(context).apply {
			onClick += ::onClickPlan
			onMenuDeleteClick = { _, _, _, _ ->
				_planDetail.updateProgress()
				true
			}
		}
		_taskList = TaskListView(context).apply {
			onClick += ::onClickTask
			onCheckChange += fun (_: Any?, _: TaskView.Companion.CheckChangeEventArgs) {
				_planDetail.updateProgress()
			}
			onMenuDeleteClick = { _, _, _, _ ->
				_planDetail.updateProgress()
				true
			}
		}


		_rootLayout.addView(_planList, _listLayoutParams)
		_rootLayout.addView(_taskList, _listLayoutParams)

		val planState = DetailState(_planDetail, _planList)
		val taskState = DetailState(_taskDetail, _taskList)

		states.register(DetailStateSwitcher.PLAN, planState)
		states.register(DetailStateSwitcher.TASK, taskState)

		states.onStateChange += ::onStateChange
		states.state = DetailStateSwitcher.PLAN

		pushPlan(mainDiBind.plan)
	}

	override fun onContextItemSelected(item: MenuItem): Boolean {
		when (item.itemId) {
			R.id.todoMenuDelete -> {
				Toast.makeText(requireContext(), "${item}", Toast.LENGTH_LONG).show()
			}
		}
		return super.onContextItemSelected(item)
	}

	fun onBack() {
		if (stack.count == 1)
			parentFragmentManager.popBackStack()
		else
			stack.pop()
	}

	fun onBackDelete(todo: ITodo, onPop: (todo: ITodo) -> Unit) {
		if (stack.count == 1) {
			val planListFragment = getPlanListFragment()
			planListFragment.delete(todo)
			parentFragmentManager.popBackStack()
		} else {
			stack.pop()
			onPop(todo)
		}
	}

	// <editor-fold desc="PRIVATE">

	private fun getPlanListFragment() = parentFragmentManager
		.findFragmentByTag(Const.FragmentTags.PLAN_LIST) as PlanListFragment

	private fun setupActivityBind() {
		activityBind?.apply {
			mainAddButton.setOnClickListener { addButtonClick(it) }
			mainDiBind.optionMenu?.apply {
				visiblePlanGroup(false)
				sortable = null
			}
			mainToolbar.setupToolbar()
		}
	}

	private fun androidx.appcompat.widget.Toolbar.setupToolbar() {
		setTitle(R.string.plan)
		setNavigationIcon(R.drawable.arrow_back)
		setNavigationOnClickListener {
			onBack()
		}
	}

	private fun addButtonClick(view: View) {
		if (states.state == DetailStateSwitcher.TASK) {
			val task = createTask?.invoke() ?: return
			pushTask(task)
		} else if (states.state == DetailStateSwitcher.PLAN) {
			val createTodoDialog = TodoCreateDialogFragment()
			createTodoDialog.setCallback {
				when (it) {
					TodoCreateDialogFragment.CREATE_TASK -> {
						val task = createTask?.invoke() ?: return@setCallback
						pushTask(task)
					}
					TodoCreateDialogFragment.CREATE_PLAN -> {
						val plan = createPlan?.invoke() ?: return@setCallback
						pushPlan(plan)
					}
				}
			}
			createTodoDialog.show(parentFragmentManager, null)
		}
	}

	// <editor-fold desc="STACK EVENTS">
	private fun onPush(any: Any?, args: TodoStackEventArgs<TodoStackElement>) {
		if (args.args == null)
			return
		when (args.args[0]) {
			PLAN -> {
				showPlanDetail(args.todo)
			}
			TASK -> {
				if (states.state == DetailStateSwitcher.PLAN)
					states.state = DetailStateSwitcher.TASK
				showTaskDetail(args.todo)
			}
		}
	}

	private fun onPop(any: Any?, args: TodoStackEventArgs<TodoStackElement>) {
		if (args.args == null)
			return
		when (args.args[0]) {
			PLAN -> {
				if (states.state == DetailStateSwitcher.TASK)
					states.state = DetailStateSwitcher.PLAN
				showPlanDetail(args.todo, true)
			}
			TASK -> {
				showTaskDetail(args.todo, true)
			}
		}
	}
	// </editor-fold>

	// <editor-fold desc="TODOS CLICK"
	private fun onClickPlan(any: Any?, args: TodoViewOnClickEventArgs) {
		pushPlan(args.todo as IPlan)
	}

	private fun onClickTask(any: Any?, args: TodoViewOnClickEventArgs) {
		pushTask(args.todo as ITask)
	}
	// </editor-fold>

	// <editor-fold desc="CHANGES">
	private fun onStateChange(any: Any?, args: StateSwitcher.Companion.StateChangeEventArgs) {
		when (args.state) {
			DetailStateSwitcher.PLAN -> {
				_planDetail.isVisible = true
				_taskDetail.isVisible = false

				_planList.isVisible = true

				createPlan = { (stack.peek.todo as IPlan).createNewPlan() }
				createTask = { (stack.peek.todo as IPlan).createNewTask() }

				activityBind?.apply {
					mainToolbar.setTitle(R.string.plan)
				}
			}
			DetailStateSwitcher.TASK -> {
				_taskDetail.isVisible = true
				_planDetail.isVisible = false

				_planList.isVisible = false

				createTask = { (stack.peek.todo as ITask).createNewTask() }
				createPlan = null

				activityBind?.apply {
					mainToolbar.setTitle(R.string.task)
				}
			}
		}
	}
	// </editor-fold>

	// <editor-fold desc="SHOWING">
	private fun showPlanDetail(todoStackElement: TodoStackElement, recompute: Boolean = false) {
		val plan = todoStackElement.todo as IPlan
		_planDetail.apply {
			bindTodo = plan
			sortable = todoStackElement
		}
		_taskDetail.apply {
			bindTodo = null
			sortable = null
		}
		_planList.apply {
			bind = plan.plans.toPlanEventDB()
			sortable = todoStackElement
		}
		_taskList.apply {
			bind = plan.todos.toTaskEventDB()
			sortable = todoStackElement
		}
		if (recompute)
			_planList.updateAll()
	}

	private fun showTaskDetail(todoStackElement: TodoStackElement, recompute: Boolean = false) {
		val task = todoStackElement.todo as ITask
		_taskDetail.apply {
			bindTodo = task
			sortable = todoStackElement
		}
		_planDetail.apply {
			bindTodo = null
			sortable = null
		}
		_taskList.apply {
			bind = task.todos.toTaskEventDB()
			sortable = todoStackElement
		}
		_planList.apply {
			bind = null
			sortable = null
		}
	}
	// </editor-fold>

	private fun pushPlan(plan: IPlan) {
		val todoStackElement = plan.getStackElement()
		stack.push(todoStackElement, arrayOf(PLAN))
	}

	private fun pushTask(task: ITask) {
		val todoStackElement = task.getStackElement()
		stack.push(todoStackElement, arrayOf(TASK))
	}
	// </editor-fold>

	companion object {

		private const val PLAN = "plan"
		private const val TASK = "task"
	}
}