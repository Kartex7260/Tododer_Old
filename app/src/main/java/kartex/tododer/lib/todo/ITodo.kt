package kartex.tododer.lib.todo

import kartex.tododer.lib.todo.visitor.ITodoResultVisitor
import kartex.tododer.lib.todo.visitor.ITodoVisitor

interface ITodo {
	val id: Int

	var parent: ITodo?
	var title: String
	var time: Long

	fun visit(visitor: ITodoVisitor)
	fun <T> resultVisit(visitor: ITodoResultVisitor<T>): T

	fun clone(): ITodo
}