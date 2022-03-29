package kartex.tododer.lib.extensions

import kartex.tododer.lib.todo.*
import java.util.*

// <editor-fold desc="Creating">
fun <Todo : ITask> ITreeTodo<Todo>.addNewTodo(task: Todo) {
	require( !todos.contains(task.id) ) { "Task ($task) contains in: $this" }
	todos.add(task)
}

fun ITreeTodo<ITask>.createNewTask(title: String = ""): Task {
	val id = todos.generateID()
	val task = Task(id, this)
	task.title = title
	todos.add(task)
	return task
}
// </editor-fold>

// <editor-fold desc="Utils">
fun <Todo : ITodo> ITreeTodo<Todo>.getTodo(id: Int): Todo? {
	for (task in todos) {
		if (task.id == id)
			return task
	}
	return null
}

fun ITreeTodo<ITodo>.contains(id: Int) = todos.contains(id)

fun Iterable<ITask>.getTodo(id: Int) = firstOrNull { it.id == id }

fun Iterable<ITask>.contains(id: Int) = contains { it.id == id }



fun ITodo.getAllParents(): List<ITodo> {
	val parents: MutableList<ITodo> = ArrayList()
	collectionAllParents(parents)
	return parents
}

fun ITodo.collectionAllParents(parents: MutableList<ITodo>) {
	val parent = parent ?: return
	parents.add(parent)
	parent.collectionAllParents(parents)
}

fun ITodo.getRoot(): ITodo {
	val parent = parent ?: return this
	return parent.getRoot()
}

fun ITodo.whatIsIT(): WIITodo {
	when (javaClass) {
		Plan::class.java -> WIITodo.PLAN
		Task::class.java -> WIITodo.TASK
	}
	return WIITodo.NONE
}
// </editor-fold>

enum class WIITodo {
	NONE,
	PLAN,
	TASK;
}
