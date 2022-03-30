package kartex.tododer.lib

import kartex.tododer.lib.todo.IPlan
import kartex.tododer.lib.todo.Plan
import kartex.tododer.ui.sort.SortByTime
import kartex.tododer.ui.sort.TodoSort

class DIProvider {
	var sort: TodoSort = SortByTime.INST
	var plan: IPlan = Plan.empty
}