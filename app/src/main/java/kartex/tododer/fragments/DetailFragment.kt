package kartex.tododer.fragments

import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.LinearLayout
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResult
import kartex.tododer.R
import kartex.tododer.lib.Const
import kartex.tododer.lib.DIProvider
import kartex.tododer.lib.StateSwitcher
import kartex.tododer.lib.extensions.toPlanEventDB
import kartex.tododer.lib.extensions.toTaskEventDB
import kartex.tododer.lib.model.IEventTodoDB
import kartex.tododer.lib.todo.IPlan
import kartex.tododer.lib.todo.ITask
import kartex.tododer.lib.todo.ITodo
import kartex.tododer.lib.todo.stack.TodoStack
import kartex.tododer.lib.todo.stack.TodoStackEventArgs
import kartex.tododer.ui.*
import kartex.tododer.ui.events.TodoViewOnClickEventArgs
import org.kodein.di.DI
import org.kodein.di.DIAware
import org.kodein.di.android.closestDI
import org.kodein.di.instance

class DetailFragment : Fragment(R.layout.fragment_todo_detail), DIAware {

	// <editor-fold desc="FIELD`S">
	override val di: DI by closestDI { requireContext() }
	private val diProvider: DIProvider by instance()

	private val states: DetailStateSwitcher = DetailStateSwitcher()

	private lateinit var stack: TodoStack<ITodo>
	// </editor-fold>

	// <editor-fold desc="VIEW`S">
	private lateinit var _planDetail: PlanDetailView
	private lateinit var _taskDetail: TaskDetailView
	private lateinit var _rootDetail: LinearLayout

	private lateinit var _planList: TodoListView<IPlan, IEventTodoDB<IPlan>>
	private lateinit var _taskList: TaskListView

	private val _listLayoutParams = LinearLayout.LayoutParams(
		LinearLayout.LayoutParams.MATCH_PARENT,
		LinearLayout.LayoutParams.WRAP_CONTENT
	)
	// </editor-fold>

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		stack = diProvider.stack

		stack.onPush += ::onPush
		stack.onPop += ::onPop
	}

	override fun onDestroy() {
		super.onDestroy()
		stack.onPush -= ::onPush
		stack.onPop -= ::onPop
		stack.clear()

		setFragmentResult(Const.ResultApiKeys.CLOSE_DETAIL, bundleOf())
	}

	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)
		val context = requireContext()

		_planDetail = view.findViewById(R.id.fragmentDetailPlan)
		_taskDetail = view.findViewById(R.id.fragmentDetailTask)
		_rootDetail = view.findViewById(R.id.fragmentDetailRoot)

		_planList = TodoListView(context)
		_taskList = TaskListView(context)
		_planList.onClick += ::onClickPlan
		_taskList.onClick += ::onClickTask
		_taskList.onCheckChange += ::onCheckChange

		_rootDetail.addView(_planList, _listLayoutParams)
		_rootDetail.addView(_taskList, _listLayoutParams)

		val planState = DetailState(_planDetail, _planList)
		val taskState = DetailState(_taskDetail, _taskList)

		states.register(DetailStateSwitcher.PLAN, planState)
		states.register(DetailStateSwitcher.TASK, taskState)

		states.onStateChange += ::onStateChange
		states.state = DetailStateSwitcher.PLAN

		stack.push(diProvider.plan, arrayOf(PLAN))
	}

	override fun onContextItemSelected(item: MenuItem): Boolean {
		when (item.itemId) {
			R.id.todoMenuDelete -> {
				Toast.makeText(requireContext(), "${item}", Toast.LENGTH_LONG).show()
			}
		}
		return super.onContextItemSelected(item)
	}

	// <editor-fold desc="PRIVATE">
	// <editor-fold desc="STACK EVENTS">
	private fun onPush(any: Any?, args: TodoStackEventArgs<ITodo>) {
		if (args.args == null)
			return
		when (args.args[0]) {
			PLAN -> {
				showPlanDetail(args.todo as IPlan)
			}
			TASK -> {
				if (states.state == DetailStateSwitcher.PLAN)
					states.state = DetailStateSwitcher.TASK
				showTaskDetail(args.todo as ITask)
			}
		}
	}

	private fun onPop(any: Any?, args: TodoStackEventArgs<ITodo>) {
		if (args.args == null)
			return
		when (args.args[0]) {
			PLAN -> {
				if (states.state == DetailStateSwitcher.TASK)
					states.state = DetailStateSwitcher.PLAN
				showPlanDetail(args.todo as IPlan, true)
			}
			TASK -> {
				showTaskDetail(args.todo as ITask, true)
			}
		}
	}
	// </editor-fold>

	// <editor-fold desc="TODOS CLICK"
	private fun onClickPlan(any: Any?, args: TodoViewOnClickEventArgs) {
		stack.push(args.todo, arrayOf(PLAN))
	}

	private fun onClickTask(any: Any?, args: TodoViewOnClickEventArgs) {
		stack.push(args.todo, arrayOf(TASK))
	}
	// </editor-fold>

	// <editor-fold desc="CHANGES">
	private fun onCheckChange(any: Any?, args: TaskView.Companion.CheckChangeEventArgs) {
		_planDetail.updateProgress()
	}

	private fun onStateChange(any: Any?, args: StateSwitcher.Companion.StateChangeEventArgs) {
		when (args.state) {
			DetailStateSwitcher.PLAN -> {
				_planDetail.isVisible = true
				_taskDetail.isVisible = false

				_planList.isVisible = true
			}
			DetailStateSwitcher.TASK -> {
				_taskDetail.isVisible = true
				_planDetail.isVisible = false

				_planList.isVisible = false
			}
		}
	}
	// </editor-fold>

	// <editor-fold desc="SHOWING">
	private fun showPlanDetail(plan: IPlan, recompute: Boolean = false) {
		_planDetail.bindTodo = plan
		_planList.bind = plan.plans.toPlanEventDB()
		_taskList.bind = plan.todos.toTaskEventDB()
		if (recompute)
			_planList.updateAll()
	}

	private fun showTaskDetail(task: ITask, recompute: Boolean = false) {
		_taskDetail.bindTodo = task
		_taskList.bind = task.todos.toTaskEventDB()
	}
	// </editor-fold>
	// </editor-fold>

	companion object {

		private const val PLAN = "plan"
		private const val TASK = "task"
	}
}