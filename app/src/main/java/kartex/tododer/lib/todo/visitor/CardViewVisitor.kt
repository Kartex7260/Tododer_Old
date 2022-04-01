package kartex.tododer.lib.todo.visitor

import android.content.Context
import kartex.tododer.lib.todo.IPlan
import kartex.tododer.lib.todo.ITask
import kartex.tododer.lib.todo.ITodo
import kartex.tododer.ui.PlanView
import kartex.tododer.ui.TaskView
import kartex.tododer.ui.TodoView

class CardViewVisitor(val context: Context) : ITodoResultVisitor<TodoView<out ITodo>> {

	override fun visitPlan(plan: IPlan): TodoView<IPlan> {
		val planView = PlanView(context)
		planView.bindTodo = plan
		return planView
	}

	override fun visitTask(task: ITask): TodoView<ITask> {
		val taskView = TaskView(context)
		taskView.bindTodo = task
		return taskView
	}
}