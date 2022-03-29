package kartex.tododer.lib.model.oid

import kartex.tododer.lib.todo.IRemarkTodo
import kartex.tododer.lib.todo.ITodo

abstract class OIDRemarkTodo(id: Int, parent: ITodo? = null) : OIDTodo(id, parent), IRemarkTodo {

	override var remark: String = ""

	abstract override fun clone(): OIDRemarkTodo
}