package kartex.tododer

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.FragmentContainerView
import androidx.fragment.app.commit
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kartex.tododer.fragments.PlanListFragment
import kartex.tododer.lib.Const
import kartex.tododer.lib.todo.*
import org.kodein.di.DIAware
import org.kodein.di.android.closestDI
import org.kodein.di.instance

import kartex.tododer.lib.Const.DITags
import kartex.tododer.lib.DIProvider
import kartex.tododer.lib.model.IEventTodoDB
import kartex.tododer.ui.dialogs.SortDialogFragment

class MainActivity : AppCompatActivity(), DIAware {

	override val di by closestDI()
	private val diProvider: DIProvider by instance()
	private val sortDialog: SortDialogFragment by instance()
	private val db: IEventTodoDB<ITodo> by instance(DITags.DB_MAIN)

	private lateinit var toolbar: Toolbar
	private lateinit var addButton: FloatingActionButton
	private lateinit var fragmentContainer: FragmentContainerView

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContentView(R.layout.activity_main)

		toolbar = findViewById(R.id.mainToolbar)
		addButton = findViewById(R.id.mainAddButton)
		fragmentContainer = findViewById(R.id.mainFragmentContainer)

		addButton.setOnClickListener {

		}

		if (savedInstanceState == null) {
			showPlanListFragment()
		}
	}

	override fun onBackPressed() {
		val stack = diProvider.stack
		if (stack.count() > 1) {
			stack.pop()
			return
		}
		super.onBackPressed()
	}

	private fun showPlanListFragment() {
		supportFragmentManager.commit {
			setReorderingAllowed(true)
			add(R.id.mainFragmentContainer, PlanListFragment::class.java, null, Const.FragmentTags.PLAN_LIST)
		}
	}
}