package kartex.tododer.lib

import kartex.tododer.ui.sort.SortByTime
import kartex.tododer.ui.sort.TodoSort

object Const {

	object MainScreen {
		const val MARGIN_HOR_DPI: Int = 15
		const val MARGIN_TOP_DPI: Int = 10
		const val MARGIN_BOTTOM_DPI: Int = 5
	}

	object DITags {
		const val DB_MAIN: String = "db_main"

		const val LP_MAIN_CARD = "lp_main_card"
	}

	object DialogTags {
		const val MAIN: String = "dialog_main"
	}

	object FragmentTags {
		const val PLAN_LIST: String = "fragment_plan_list"
		const val DETAIL: String = "detail"
	}

	object ResultApiKeys {

		const val CLOSE_DETAIL = "closeDetail"
	}

	val SORT: TodoSort = SortByTime.INST

	const val BACK_STACK_MAIN: String = "bs_main"
}