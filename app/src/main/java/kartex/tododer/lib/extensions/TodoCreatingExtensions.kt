package kartex.tododer.lib.extensions

import kartex.tododer.lib.todo.*

fun ITreeTodo<ITask>.createNewTask(title: String = ""): Task {
	val id = todos.generateID()
	val task = Task(id, this)
	task.title = title
	todos.add(task)
	return task
}

fun IPlan.createNewPlan(title: String = ""): Plan {
	val id = plans.generateID()
	val plan = Plan(id, this)
	plan.title = title
	plans.add(plan)
	return plan
}