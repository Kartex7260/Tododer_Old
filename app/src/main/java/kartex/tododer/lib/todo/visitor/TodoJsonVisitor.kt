package kartex.tododer.lib.todo.visitor

import kartex.tododer.lib.extensions.toDto
import kartex.tododer.lib.extensions.toJson
import kartex.tododer.lib.todo.IPlan
import kartex.tododer.lib.todo.ITask
import kartex.tododer.lib.todo.ITodo
import kartex.tododer.lib.todo.dto.TodoDTO
import java.lang.IllegalStateException

class TodoJsonVisitor : ITodoResultVisitor<String> {

	override fun visitPlan(plan: IPlan): String {
		val dto = plan.toDto()
		return dto.toJson()
	}

	override fun visitTask(task: ITask): String {
		val dto = task.toDto()
		return dto.toJson()
	}
}