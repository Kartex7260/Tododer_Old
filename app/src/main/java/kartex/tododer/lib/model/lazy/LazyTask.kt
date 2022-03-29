package kartex.tododer.lib.model.lazy

import kartex.tododer.lib.extensions.cloneWithoutChildren
import kartex.tododer.lib.extensions.fillTasks
import kartex.tododer.lib.todo.ITask
import kartex.tododer.lib.todo.dto.TaskDTO

class LazyTask(dir: String, task: ITask, dto: TaskDTO) : LazyTreeTodo<ITask, ITask, TaskDTO>(dir, task, dto), ITask {

	override var check: Boolean
		get() = todo.check
		set(value) {
			todo.check = value
		}

	override fun clone() = todo.clone()

	override fun toFull(): ITask {
		val task = todo.cloneWithoutChildren()
		task.fillTasks(dir, dto)
		return task
	}
}