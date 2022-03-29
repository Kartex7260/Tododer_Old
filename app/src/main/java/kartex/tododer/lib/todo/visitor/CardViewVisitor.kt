package kartex.tododer.lib.todo.visitor

import android.content.Context
import android.view.View
import kartex.tododer.lib.todo.IPlan
import kartex.tododer.lib.todo.ITask
import kartex.tododer.ui.PlanView
import kartex.tododer.ui.TaskView

class CardViewVisitor(val context: Context) : ITodoResultVisitor<View> {

	override fun visitPlan(plan: IPlan): View {
		val planView = PlanView(context)
		planView.bindTodo = plan
		return planView
	}

	override fun visitTask(task: ITask): View {
		val taskView = TaskView(context)
		taskView.bindTodo = task
		return taskView
	}
}