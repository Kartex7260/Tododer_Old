package kartex.tododer

import android.app.Application
import android.view.ViewGroup
import android.widget.LinearLayout
import kartex.tododer.lib.Const
import kartex.tododer.lib.DIProvider
import kartex.tododer.lib.extensions.toDpi
import kartex.tododer.lib.model.CachePlanDB
import kartex.tododer.lib.model.EventTodoDB
import kartex.tododer.lib.model.IEventTodoDB
import kartex.tododer.lib.todo.*
import kartex.tododer.ui.dialogs.SortDialogFragment
import org.kodein.di.DI
import org.kodein.di.DIAware
import org.kodein.di.bindSingleton

class TododerApplication : Application(), DIAware {

	private val db: CachePlanDB = CachePlanDB()

	override val di by DI.lazy {
		bindSingleton<IEventTodoDB<IPlan>>(Const.DITags.DB_MAIN) {
			EventTodoDB(db)
		}

		bindSingleton<ViewGroup.LayoutParams>(Const.DITags.LP_MAIN_CARD) {
			val ths = this@TododerApplication
			val marTop = Const.MainScreen.MARGIN_TOP_DPI.toDpi(ths)
			val marHor = Const.MainScreen.MARGIN_HOR_DPI.toDpi(ths)
			val marBot = Const.MainScreen.MARGIN_BOTTOM_DPI.toDpi(ths)

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
	}
}