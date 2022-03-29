package kartex.tododer.lib.todo.dto.oid

import kartex.tododer.lib.todo.dto.ITodosParent

open class OIDTreeTodoDTO : OIDTodoDTO(), ITodosParent {

	override var todosCUIDS: MutableList<String> = ArrayList()
}