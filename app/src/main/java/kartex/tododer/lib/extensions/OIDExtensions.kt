package kartex.tododer.lib.extensions

import kartex.tododer.lib.model.lazy.LazyPlan
import kartex.tododer.lib.model.lazy.LazyTask
import kartex.tododer.lib.model.lazy.LazyTreeTodo
import kartex.tododer.lib.model.oid.OIDPlan
import kartex.tododer.lib.model.oid.OIDTask
import kartex.tododer.lib.todo.IPlan
import kartex.tododer.lib.todo.ITask
import kartex.tododer.lib.todo.ITodo
import kartex.tododer.lib.todo.ITreeTodo
import kartex.tododer.lib.todo.dto.IPlansParent
import kartex.tododer.lib.todo.dto.ITodosParent
import kartex.tododer.lib.todo.dto.TreeTodoDTO
import kartex.tododer.lib.todo.dto.oid.OIDPlanDTO
import kartex.tododer.lib.todo.dto.oid.OIDTaskDTO

fun ITask.toOID(): OIDTask {
	val oidTask = OIDTask(id, parent)
	for (task in todos) {
		val oid = task.toOID()
		oidTask.todos.add(oid)
	}
	return oidTask
}

fun IPlan.toOID(): OIDPlan {
	val oidPlan = OIDPlan(id, parent)
	for (plan in plans) {
		val oid = plan.toOID()
		oidPlan.plans.add(oid)
	}
	for (task in todos) {
		val oid = task.toOID()
		oidPlan.todos.add(oid)
	}
	return oidPlan
}

// <editor-fold desc="oid full reading">
fun LazyPlan.toFullOID(dir: String, parent: ITodo? = null): OIDPlan {
	val cuid = getCUID()
	val dto = readTodo(dir, cuid, ::jsonToOIDPlanDTO)
	val oidPlan = dto.toOIDPlan(parent)
	oidPlan.fillFullOIDPlans(dir, dto)
	return oidPlan
}

fun LazyTask.toFullOID(dir: String): OIDTask {
	val cuid = getCUID()
	val dto = readTodo(dir, cuid, ::jsonToOIDTaskDTO)
	val oidTask = dto.toOIDTask(this)
	oidTask.fillFullOIDTasks(dir, dto)
	return oidTask
}
// </editor-fold>

// <editor-fold desc="FILLING">
fun <Todo : ITreeTodo<ITask>, DTO : TreeTodoDTO> LazyTreeTodo<ITask, Todo, DTO>.fillOIDTasksFromCache() {
	for (cuid in dto.todosCUIDS) {
		val id = getID(cuid)
		val oidTask = OIDTask(id, todo)
		todo.todos.add(oidTask)
	}
}

fun LazyPlan.fillOIDPlansFromCache() {
	for (cuid in dto.plansCUIDS) {
		val id = getID(cuid)
		val oidTask = OIDPlan(id, todo)
		todo.plans.add(oidTask)
	}

	fillOIDTasksFromCache()
}

fun IPlan.fillFullOIDPlans(dir: String, plansParent: IPlansParent, rec: Boolean = true) {
	for (cuid in plansParent.plansCUIDS) {
		val oidDto = readTodo(dir, cuid, ::jsonToOIDPlanDTO)
		val oidPlan = oidDto.toOIDPlan(this)
		if (rec)
			oidPlan.fillFullOIDPlans(dir, oidDto, rec)
		plans.add(oidPlan)
	}

	fillFullOIDTasks(dir, plansParent, rec)
}

fun ITreeTodo<ITask>.fillFullOIDTasks(dir: String, todosParent: ITodosParent, rec: Boolean = true) {
	for (cuid in todosParent.todosCUIDS) {
		val oidDto = readTodo(dir, cuid, ::jsonToOIDTaskDTO)
		val oidTask = oidDto.toOIDTask(this)
		if (rec)
			oidTask.fillFullOIDTasks(dir, oidDto, rec)
		todos.add(oidTask)
	}
}
// </editor-fold>

// <editor-fold desc="private">
private fun getID(cuid: String): Int {
	val ids = cuid.split('-')
	val lastId = ids.last()
	return Integer.parseInt(lastId)
}

// JSON -> DTO
private fun jsonToOIDPlanDTO(json: String): OIDPlanDTO {
	val gson = getGsonInstance()
	return gson.fromJson(json, OIDPlanDTO::class.java)
}

private fun jsonToOIDTaskDTO(json: String): OIDTaskDTO {
	val gson = getGsonInstance()
	return gson.fromJson(json, OIDTaskDTO::class.java)
}

// DTO -> OBJECT
private fun OIDPlanDTO.toOIDPlan(parent: ITodo? = null) = OIDPlan(id, parent)

private fun OIDTaskDTO.toOIDTask(parent: ITodo? = null) = OIDTask(id, parent)
// </editor-fold>