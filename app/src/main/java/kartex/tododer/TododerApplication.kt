package kartex.tododer

import android.app.Application
import android.view.ViewGroup
import android.widget.LinearLayout
import kartex.tododer.lib.extensions.createNewTask
import kartex.tododer.lib.extensions.createPlan
import kartex.tododer.lib.extensions.toDpi
import kartex.tododer.lib.model.CachePlanDB
import kartex.tododer.lib.model.ITodoDB
import kartex.tododer.lib.todo.IPlan
import org.kodein.di.DI
import org.kodein.di.DIAware
import org.kodein.di.bindSingleton

import kartex.tododer.lib.Const.DITags
import kartex.tododer.lib.Const.MainScreen
import kartex.tododer.lib.DIProvider
import kartex.tododer.lib.todo.ITodo
import kartex.tododer.ui.dialogs.SortDialogFragment

class TododerApplication : Application(), DIAware {

	private val db: CachePlanDB = CachePlanDB()

	override val di by DI.lazy {
		bindSingleton<ITodoDB<out ITodo>>(DITags.DB_MAIN) { db }

		bindSingleton<ViewGroup.LayoutParams>(DITags.LP_MAIN_CARD) {
			val ths = this@TododerApplication
			val marTop = MainScreen.MARGIN_TOP_DPI.toDpi(ths)
			val marHor = MainScreen.MARGIN_HOR_DPI.toDpi(ths)
			val marBot = MainScreen.MARGIN_BOTTOM_DPI.toDpi(ths)

			val layoutParams = LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.MATCH_PARENT,
				LinearLayout.LayoutParams.WRAP_CONTENT
			)
			layoutParams.setMargins(marHor, marTop, marHor, marBot)
			layoutParams
		}

		bindSingleton { SortDialogFragment() }
		bindSingleton { DIProvider() }
	}

	override fun onCreate() {
		super.onCreate()

		db.createPlan("Test plan 1 (No remark)")
		db.createPlan("Test plan 2 (Remark)").remark = "This is plan remark"

		val plan0 = db.createPlan("Test plan 3 (0%)")
		plan0.createNewTask()
		val plan25 = db.createPlan("Test plan 4 (25%)")
		plan25.createNewTask().check = true
		plan25.createNewTask()
		plan25.createNewTask()
		plan25.createNewTask()
		val plan50 = db.createPlan("Test plan 5 (50%)")
		plan50.createNewTask().check = true
		plan50.createNewTask()
		val plan75 = db.createPlan("Test plan 6 (75%)")
		plan75.createNewTask().check = true
		plan75.createNewTask().check = true
		plan75.createNewTask().check = true
		plan75.createNewTask()
		val plan100 = db.createPlan("Test plan 7 (100%)")
		plan100.createNewTask().check = true
	}
}