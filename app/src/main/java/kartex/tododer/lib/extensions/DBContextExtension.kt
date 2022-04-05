package kartex.tododer.lib.extensions

import kartex.tododer.lib.model.ITodoDB
import kartex.tododer.lib.todo.IPlan
import kartex.tododer.lib.todo.ITodo
import kartex.tododer.lib.todo.Plan
import java.util.*
import kotlin.random.Random

fun Iterable<ITodo>.contains(id: Int): Boolean {
	for (todo in this) {
		if (todo.id == id)
			return true
	}
	return false
}

fun Iterable<ITodo>.generateID(): Int {
	val rand = Random(Calendar.getInstance().timeInMillis)
	var result: Int? = null
	var temp: Int
	while (result == null) {
		temp = rand.nextInt()
		if (contains(temp))
			continue

		result = temp
	}
	return result
}

fun <T : ITodo> Iterable<T>.get(id: Int): T? {
	for (todo in this) {
		if (todo.id == id)
			return todo
	}
	return null
}

fun ITodoDB<IPlan>.createPlan(title: String = ""): Plan {
	val id = generateID()
	val plan = Plan(id)
	plan.title = title
	add(plan)
	return plan
}