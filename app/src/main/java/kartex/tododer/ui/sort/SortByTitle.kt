package kartex.tododer.ui.sort

import kartex.tododer.R
import kartex.tododer.lib.todo.ITodo

class SortByTitle private constructor(reverse: Boolean = false) : TodoSort(0, R.drawable.sort_alphabet_24, reverse) {

	override fun <T : ITodo> sort0(iterable: Iterable<T>) = iterable.sortedBy { it.title.lowercase() }

	override fun asReverse() = if (reverse)
		INST
	else
		INST_REVERSE

	companion object {

		val INST: SortByTitle = SortByTitle()
		val INST_REVERSE: SortByTitle = SortByTitle(true)
	}
}