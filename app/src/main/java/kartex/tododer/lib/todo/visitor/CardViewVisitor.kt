package kartex.tododer.lib.todo.visitor

import android.content.Context
import android.view.View
import kartex.tododer.lib.todo.IPlan
import kartex.tododer.lib.todo.ITask
import kartex.tododer.lib.todo.ITodo
import kartex.tododer.ui.PlanView
import kartex.tododer.ui.TaskView
import kartex.tododer.ui.TodoView

class CardViewVisitor(val context: Context) : ITodoResultVisitor<TodoView<out ITodo>> {

	override fun visitPlan(plan: IPlan): TodoView<out ITodo> = PlanView.from(context, plan)

	override fun visitTask(task: ITask): TodoView<out ITodo> = TaskView.from(context, task)
}