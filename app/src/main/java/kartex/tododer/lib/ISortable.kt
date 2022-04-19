package kartex.tododer.lib

import kartex.tododer.ui.sort.TodoSort
import savvy.toolkit.Event

interface ISortable {

	val onChangeSort: Event<ValueEventArgs<TodoSort>>
	var sort: TodoSort
}