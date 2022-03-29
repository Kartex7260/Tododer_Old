package kartex.tododer.lib.todo.visitor

import kartex.tododer.lib.extensions.toDto
import kartex.tododer.lib.extensions.toJson
import kartex.tododer.lib.todo.IPlan
import kartex.tododer.lib.todo.ITask
import java.lang.IllegalStateException

class TodoJsonVisitor : ITodoVisitor {

	private var _json: String? = null

	val json: String
		get() {
			if (_json == null)
				throw IllegalStateException("json is null")
			return _json!!
		}

	override fun visitPlan(plan: IPlan) {
		val dto = plan.toDto()
		_json = dto.toJson()
	}

	override fun visitTask(task: ITask) {
		val dto = task.toDto()
		_json = dto.toJson()
	}
}