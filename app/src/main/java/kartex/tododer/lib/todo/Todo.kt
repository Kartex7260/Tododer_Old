package kartex.tododer.lib.todo

import kartex.tododer.lib.extensions.getCUID
import kartex.tododer.lib.todo.visitor.ITodoResultVisitor
import kartex.tododer.lib.todo.visitor.ITodoVisitor
import java.util.*
import kotlin.collections.ArrayList

abstract class Todo(override val id: Int, override var parent: ITodo? = null) : ITodo {

	override var time: Long = Date().time
	override var title: String = ""

	override fun toString(): String {
		return "title: $title"
	}

	override fun equals(other: Any?): Boolean {
		if (other == null || other !is Todo)
			return false
		return other.hashCode() == hashCode()
	}

	override fun hashCode(): Int {
		return id
	}
}