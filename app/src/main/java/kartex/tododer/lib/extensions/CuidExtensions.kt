package kartex.tododer.lib.extensions

import kartex.tododer.lib.todo.ITodo
import kartex.tododer.lib.todo.visitor.UIDVisitor

private val uidVisitor = UIDVisitor()

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
	val cuidBuilder = getParentsCUIDBuilder()
	addCUIDNode(cuidBuilder)
	return cuidBuilder
}

fun ITodo.addCUIDNode(stringBuilder: StringBuilder) {
	val uid = getUID()
	stringBuilder.append(uid)
	stringBuilder.append('-')
}

fun ITodo.addCUIDClosure(stringBuilder: StringBuilder): String {
	val uid = getUID()
	stringBuilder.append(uid)
	return stringBuilder.toString()
}

fun ITodo.getUID(): String = resultVisit(uidVisitor)