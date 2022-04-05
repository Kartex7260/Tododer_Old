package kartex.tododer

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.ViewGroup
import android.widget.LinearLayout
import kartex.tododer.lib.Const
import kartex.tododer.lib.DIProvider
import kartex.tododer.lib.extensions.createNewTask
import kartex.tododer.lib.extensions.createPlan
import kartex.tododer.lib.extensions.toDpi
import kartex.tododer.lib.model.CachePlanDB
import kartex.tododer.lib.model.EventTodoDB
import kartex.tododer.lib.model.IEventTodoDB
import kartex.tododer.lib.model.ITodoDB
import kartex.tododer.lib.todo.IPlan
import kartex.tododer.lib.todo.ITodo
import kartex.tododer.ui.dialogs.SortDialogFragment
import org.kodein.di.DI
import org.kodein.di.DIAware
import org.kodein.di.android.closestDI
import org.kodein.di.bindSingleton
import org.kodein.di.instance

class LaunchActivity : AppCompatActivity(), DIAware {

	override val di by closestDI()
	private val db: IEventTodoDB<IPlan> by instance(Const.DITags.DB_MAIN)

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContentView(R.layout.activity_launch)

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

		finish()
		val intent = Intent(this, MainActivity::class.java)
		startActivity(intent)
	}
}