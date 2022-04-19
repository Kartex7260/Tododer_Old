package kartex.tododer.lib

import kartex.tododer.ui.sort.TodoSort
import savvy.toolkit.Event

interface ISortable {

	val onChangeSort: Event<DIProviderEventArgs<TodoSort>>
	var sort: TodoSort
}