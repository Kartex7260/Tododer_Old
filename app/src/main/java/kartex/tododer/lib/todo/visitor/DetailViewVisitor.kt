package kartex.tododer.lib.todo.visitor

import android.content.Context
import android.view.View
import kartex.tododer.lib.todo.IPlan
import kartex.tododer.lib.todo.ITask
import kartex.tododer.ui.PlanDetailView
import kartex.tododer.ui.TaskDetailView

class DetailViewVisitor(val context: Context) : ITodoResultVisitor<View> {

	override fun visitPlan(plan: IPlan): View {
		val planDetailView = PlanDetailView(context)
		planDetailView.bindTodo = plan
		return planDetailView
	}

	override fun visitTask(task: ITask): View {
		val taskDetailView = TaskDetailView(context)
		taskDetailView.bindTodo = task
		return taskDetailView
	}
}