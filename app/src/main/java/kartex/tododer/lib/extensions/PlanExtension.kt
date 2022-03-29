package kartex.tododer.lib.extensions

import kartex.tododer.lib.todo.IPlan

fun IPlan.getPlan(id: Int): IPlan? {
	for (plan in plans) {
		if (plan.id == id) {
			return plan
		}
	}
	return null
}