package kartex.tododer.lib.extensions

import kartex.tododer.lib.model.lazy.LazyTask
import kartex.tododer.lib.model.lazy.LazyTreeTodo
import kartex.tododer.lib.todo.ITask
import kartex.tododer.lib.todo.ITreeTodo
import kartex.tododer.lib.todo.dto.TreeTodoDTO

// <editor-fold desc="LazyTreeTodo ext">
fun <Todo : ITreeTodo<ITask>, DTO : TreeTodoDTO> LazyTreeTodo<ITask, Todo, DTO>.nextTask(index: Int): LazyTask {
	val cuid = dto.todosCUIDS[index]
	return nextTask(cuid)
}

fun <Todo : ITreeTodo<ITask>, DTO : TreeTodoDTO> LazyTreeTodo<ITask, Todo, DTO>.nextTask(cuid: String): LazyTask {
	val dto = readTodo(dir, cuid, ::jsonToTaskDTO)
	val task = dto.toTask(todo)
	val lazyTask = LazyTask(dir, task, dto)
	lazyTask.fillOIDTasksFromCache()
	return lazyTask
}

fun <Todo : ITreeTodo<ITask>, DTO : TreeTodoDTO> LazyTreeTodo<ITask, Todo, DTO>.nextTasks(): List<LazyTask> {
	val result = ArrayList<LazyTask>()
	for (cuid in dto.todosCUIDS) {
		val lazyTask = nextTask(cuid)
		result.add(lazyTask)
	}
	return result
}

fun <Todo : ITreeTodo<ITask>, DTO : TreeTodoDTO> LazyTreeTodo<ITask, Todo, DTO>.nextFullTask(cuid: String): ITask {
	val lazy = nextTask(cuid)
	return lazy.toFull()
}

fun <Todo : ITreeTodo<ITask>, DTO : TreeTodoDTO> LazyTreeTodo<ITask, Todo, DTO>.nextFullTasks(): List<ITask> {
	val tasks = ArrayList<ITask>()
	for (cuid in dto.todosCUIDS) {
		val task = nextFullTask(cuid)
		tasks.add(task)
	}
	return tasks
}
// </editor-fold>