package kartex.tododer.lib.extensions

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import kartex.tododer.lib.todo.*
import kartex.tododer.lib.todo.dto.*
import kartex.tododer.lib.todo.visitor.TodoChildRemoveVisitor
import kartex.tododer.lib.todo.visitor.TodoJsonVisitor
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream

private val gson: Gson = GsonBuilder().setLenient().create()

internal fun getGsonInstance() = gson

// <editor-folding desc="JSON - OBJECT">
fun jsonToTask(json: String, parent: ITodo? = null): Task {
	val dto = jsonToTaskDTO(json)
	return dto.toTask(parent)
}

fun jsonToPlan(json: String, parent: ITodo? = null): Plan {
	val dto = jsonToPlanDTO(json)
	return dto.toPlan(parent)
}

fun ITodo.toJson(): String {
	val jsonVisitor = TodoJsonVisitor()
	visit(jsonVisitor)
	return jsonVisitor.json
}
// </editor-fold>

// <editor-fold desc="JSON - DTO">
fun jsonToTaskDTO(json: String): TaskDTO {
	return gson.fromJson(json, TaskDTO::class.java)
}

fun jsonToPlanDTO(json: String): PlanDTO {
	return gson.fromJson(json, PlanDTO::class.java)
}

fun TaskDTO.toJson(): String {
	return gson.toJson(this)
}

fun PlanDTO.toJson(): String {
	return gson.toJson(this)
}
// </editor-fold>

// <editor-fold desc="DTO - OBJECT">
fun TaskDTO.toTask(parent: ITodo? = null): Task {
	val task = Task(id, parent)
	task.title = title
	task.time = time
	
	task.remark = remark
	task.check = check
	return task
}

fun PlanDTO.toPlan(parent: ITodo? = null): Plan {
	val plan = Plan(id, parent)
	plan.title = title
	plan.time = time
	
	plan.remark = remark
	return plan
}

fun ITask.toDto(): TaskDTO {
	val dto = TaskDTO()
	dto.id = id
	dto.title = title
	dto.time = time
	
	dto.remark = remark
	dto.check = check

	val cuid = getCUID()
	for (task in todos) {
		dto.todosCUIDS.add("$cuid-$id")
	}
	return dto
}

fun IPlan.toDto(): PlanDTO {
	val dto = PlanDTO()
	dto.id = id
	dto.title = title
	dto.time = time
	
	dto.remark = remark

	val cuid = getCUID()
	for (task in todos) {
		dto.todosCUIDS.add("$cuid-$id")
	}
	for (plan in plans) {
		dto.plansCUIDS.add("$cuid-$id")
	}
	return dto
}
// </editor-fold>

// <editor-fold desc="CUID">
fun ITodo.getCUID(): String {
	val stringBuilder = getParentsCUIDBuilder()
	return addCUIDClosure(stringBuilder)
}

fun ITodo.getParentsCUIDBuilder(): StringBuilder {
	val parents = getAllParents()
	val stringBuilder = StringBuilder()
	for (parent in parents.asReversed()) {
		parent.addCUIDNode(stringBuilder)
	}
	return stringBuilder
}

fun ITodo.getCUIDBuilder(): StringBuilder {
	val parents = getAllParents()
	val stringBuilder = StringBuilder()
	for (parent in parents.asReversed()) {
		parent.addCUIDNode(stringBuilder)
	}
	addCUIDNode(stringBuilder)
	return stringBuilder
}

fun ITodo.addCUIDNode(stringBuilder: StringBuilder) {
	stringBuilder.append(id)
	stringBuilder.append('-')
}

fun ITodo.addCUIDClosure(stringBuilder: StringBuilder): String {
	stringBuilder.append(id)
	return stringBuilder.toString()
}
// </editor-fold>

// <editor-fold desc="saving to file">
fun ITodo.saveToDisk(dir: String, overwrite: Boolean = true) {
	val cuid = getCUID()
	val file = todoFile(dir, cuid)
	saveToDisk(file)
}

fun ITodo.saveToDisk(file: File, overwrite: Boolean = true) {
	if (file.exists() && overwrite) {
		file.deleteOnExit()
		file.createNewFile()
	}
	val stream = file.outputStream()
	saveToDisk(stream)
}

fun ITodo.saveToDisk(stream: FileOutputStream) {
	val buffer = toJson().toByteArray()
	stream.write(buffer)
	stream.flush()
}
// </editor-fold>

// <editor-fold desc="read from file">
fun <DTO> readTodo(dir: String, id: String, convert: (String) -> DTO): DTO {
	val file = todoFile(dir, id)
	return readTodo(file, convert)
}

fun <DTO> readTodo(path: String, convert: (String) -> DTO): DTO {
	val file = File(path)
	return readTodo(file, convert)
}

fun <DTO> readTodo(file: File, convert: (String) -> DTO): DTO {
	val stream = file.inputStream()
	return readTodo(stream, convert)
}

fun <DTO> readTodo(stream: FileInputStream, convert: (String) -> DTO): DTO {
	val buffer = ByteArray(stream.available())
	stream.read(buffer)
	val json = buffer.toString()
	return convert(json)
}

fun ITreeTodo<ITask>.fillTasks(dir: String, todosParents: ITodosParent, rec: Boolean = true) {
	if (todosParents.todosCUIDS.count() == 0)
		return

	for (cuid in todosParents.todosCUIDS) {
		val childDTO = readTodo(dir, cuid, ::jsonToTaskDTO)
		val task = childDTO.toTask(this)

		if (rec)
			task.fillTasks(dir, childDTO)

		todos.add(task)
	}
}

fun IPlan.fillPlans(dir: String, plansParent: IPlansParent, rec: Boolean = true) {
	if (plansParent.todosCUIDS.count() == 0 && plansParent.plansCUIDS.count() == 0)
		return

	for (cuid in plansParent.plansCUIDS) {
		val childDTO = readTodo(dir, cuid, ::jsonToPlanDTO)
		val plan = childDTO.toPlan(this)

		if (rec)
			plan.fillPlans(dir, childDTO)

		plans.add(plan)
	}

	fillTasks(dir, plansParent, rec)
}
// </editor-fold>

// <editor-fold desc="remove">
fun ITodo.removeFromDisk(dir: String, rec: Boolean = true): Boolean {
	val cuidBuilder = getCUIDBuilder()
	val file = todoFile(dir, cuidBuilder.toString())
	val deleteResult = file.delete()

	if (rec) {
		val visitor = TodoChildRemoveVisitor(dir, cuidBuilder)
		visit(visitor)
	}

	return deleteResult
}
// </editor-fold>

fun todoFile(dir: String, id: String) = File(dir, "$id.json")