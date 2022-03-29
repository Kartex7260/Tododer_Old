package kartex.tododer.lib.model.oid

import kartex.tododer.lib.todo.ITodo

abstract class OIDTodo(override val id: Int, override var parent: ITodo? = null) : ITodo {

	override var title: String
		get() = ""
		set(value) {}

	override var time: Long
		get() = 0
		set(value) {}

	abstract override fun clone(): OIDTodo
}