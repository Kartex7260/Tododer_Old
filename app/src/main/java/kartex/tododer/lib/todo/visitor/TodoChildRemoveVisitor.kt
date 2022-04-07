package kartex.tododer.lib.todo.visitor

import kartex.tododer.lib.todo.IPlan
import kartex.tododer.lib.todo.ITask
import kartex.tododer.lib.todo.ITodo
import kartex.tododer.lib.todo.ITreeTodo
import java.lang.StringBuilder
import java.nio.file.Files
import java.nio.file.Paths

class TodoChildRemoveVisitor(private val dir: String, private val cuidBuilder: StringBuilder) : ITodoVisitor {

	override fun visitPlan(plan: IPlan) {
		removePlans(plan, cuidBuilder.toString())
	}

	override fun visitTask(task: ITask) {
		removeTasks(task, cuidBuilder.toString())
	}

	// <editor-fold desc="PRIVATE">
	private fun removeTasks(todo: ITreeTodo<ITask>, parentsCUID: String) {
		for (task in todo.todos) {
			val cuid = parentsCUID + task.id
			deleteFile(cuid)
			removeTasks(task, "$cuid-")
		}
	}

	private fun removePlans(parentPlan: IPlan, parentsCUID: String) {
		for (plan in parentPlan.plans) {
			val cuid = parentsCUID + plan.id
			deleteFile(cuid)
			removePlans(plan, "$cuid-")
		}
		removeTasks(parentPlan, parentsCUID)
	}

	private fun deleteFile(cuid: String) {
		val path = Paths.get(dir, cuid)
		Files.delete(path)
	}
	// </editor-fold>
}