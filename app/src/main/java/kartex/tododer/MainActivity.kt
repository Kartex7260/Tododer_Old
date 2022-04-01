package kartex.tododer

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.FragmentContainerView
import androidx.fragment.app.commit
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kartex.tododer.fragments.TodoListFragment
import kartex.tododer.lib.Const
import kartex.tododer.lib.model.ITodoDB
import kartex.tododer.lib.todo.*
import org.kodein.di.DIAware
import org.kodein.di.android.closestDI
import org.kodein.di.instance

import kartex.tododer.lib.Const.DITags
import kartex.tododer.ui.dialogs.SortDialogFragment

class MainActivity : AppCompatActivity(), DIAware {

	override val di by closestDI()
	private val sortDialog: SortDialogFragment by instance()
	private val db: ITodoDB<ITodo> by instance(DITags.DB_MAIN)

	private lateinit var toolbar: Toolbar
	private lateinit var addButton: FloatingActionButton
	private lateinit var fragmentContainer: FragmentContainerView

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContentView(R.layout.activity_main)

		toolbar = findViewById(R.id.mainToolbar)
		addButton = findViewById(R.id.mainAddButton)
		fragmentContainer = findViewById(R.id.mainFragmentContainer)
		showPlanListFragment()

		addButton.setOnClickListener {

		}
	}

	private fun showPlanListFragment() {
		supportFragmentManager.commit {
			setReorderingAllowed(true)
			add(R.id.mainFragmentContainer, null, Const.FragmentTags.MAIN)
			addToBackStack(Const.BACK_STACK_MAIN)
		}
	}
}