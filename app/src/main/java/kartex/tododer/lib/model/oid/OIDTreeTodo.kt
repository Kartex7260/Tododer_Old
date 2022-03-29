package kartex.tododer.lib.model.oid

import kartex.tododer.lib.todo.ITodo
import kartex.tododer.lib.todo.ITreeTodo

abstract class OIDTreeTodo<TreeType : ITodo>(id: Int, parent: ITodo? = null) : OIDRemarkTodo(id, parent), ITreeTodo<TreeType> {

	override val todos: MutableList<TreeType> = ArrayList()

	abstract override fun clone(): OIDTreeTodo<TreeType>
}