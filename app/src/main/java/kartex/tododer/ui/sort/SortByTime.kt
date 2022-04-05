package kartex.tododer.ui.sort

import kartex.tododer.R
import kartex.tododer.lib.todo.ITodo

class SortByTime private constructor(reverse: Boolean = false) : TodoSort(1, R.drawable.filter_time_24, reverse) {

	override fun <T : ITodo> sort0(iterable: Iterable<T>) = iterable.sortedBy { it.time }

	override fun asReverse() =  if (reverse)
			INST
		else
			INST_REVERSE

	companion object {

		val INST: SortByTime = SortByTime()
		val INST_REVERSE: SortByTime = SortByTime(true)
	}
}