package kartex.tododer.lib.todo.stack

import kartex.tododer.lib.todo.ITodo
import savvy.toolkit.Event
import java.util.*

class TodoStack<Todo : ITodo> {

	private val stack: Stack<TodoArgs<Todo>> = Stack()
	private val eventLocker: Any = Any()

	val onPush: Event<TodoStackEventArgs<Todo>> = Event(eventLocker)
	val onPop: Event<TodoStackEventArgs<Todo>> = Event(eventLocker)

	fun push(todo: Todo, args: Array<Any>? = null): Todo {
		val todoArgs = TodoArgs(todo, args)
		stack.push(todoArgs)
		val eventArgs = TodoStackEventArgs(todo, args)
		onPush.invoke(eventLocker, eventArgs)
		return todo
	}

	fun pop(): Todo {
		val result = stack.pop()
		val peak = stack.peek()
		val eventArgs = TodoStackEventArgs(peak.todo, peak.args)
		onPop.invoke(eventLocker, eventArgs)
		return result.todo
	}

	fun peek() = stack.peek().todo

	fun empty() = stack.empty()

	fun count() = stack.count()

	fun search(any: Todo) = stack.search(TodoArgs(any))

	fun clear() = stack.clear()

	private class TodoArgs<Todo : ITodo>(val todo: Todo, val args: Array<Any>? = null) {
		override fun equals(other: Any?): Boolean {
			if (other == null || other !is TodoArgs<*>)
				return false
			return hashCode() == other.hashCode()
		}

		override fun hashCode(): Int {
			return todo.hashCode()
		}
	}
}