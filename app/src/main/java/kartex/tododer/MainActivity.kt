package kartex.tododer

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.FragmentContainerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kartex.tododer.lib.Const
import kartex.tododer.lib.model.ITodoDB
import kartex.tododer.lib.todo.*
import org.kodein.di.DIAware
import org.kodein.di.android.closestDI
import org.kodein.di.instance

import kartex.tododer.lib.Const.DITags
import kartex.tododer.ui.PlanDetailView
import kartex.tododer.ui.PlanView
import kartex.tododer.ui.TaskDetailView
import kartex.tododer.ui.TreeTodoDetailView
import kartex.tododer.ui.dialogs.SortDialogFragment

class MainActivity : AppCompatActivity(), DIAware {

	override val di by closestDI()
	private val sortDialog: SortDialogFragment by instance()

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
	}
}