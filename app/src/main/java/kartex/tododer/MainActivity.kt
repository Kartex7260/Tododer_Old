package kartex.tododer

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import androidx.fragment.app.commit
import kartex.tododer.databinding.ActivityMainBinding
import kartex.tododer.extensions.getColorOnPrimary
import kartex.tododer.fragments.PlanListFragment
import kartex.tododer.lib.Const
import kartex.tododer.lib.todo.*
import org.kodein.di.DIAware
import org.kodein.di.android.closestDI
import org.kodein.di.instance

import kartex.tododer.lib.Const.DITags
import kartex.tododer.lib.MainDIBind
import kartex.tododer.lib.model.IEventTodoDB
import kartex.tododer.menus.MainOptionMenu
import kartex.tododer.ui.dialogs.SortDialogFragment

class MainActivity : AppCompatActivity(), DIAware {

	override val di by closestDI()
	private val mainDiBind: MainDIBind by instance()
	private val sortDialog: SortDialogFragment by instance()
	private val db: IEventTodoDB<ITodo> by instance(DITags.DB_MAIN)

	private lateinit var mainOptionMenu: MainOptionMenu

	private lateinit var _bind: ActivityMainBinding

	val bind: ActivityMainBinding
		get() = _bind

	@SuppressLint("UseCompatLoadingForDrawables")
	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		_bind = ActivityMainBinding.inflate(layoutInflater)
		setContentView(_bind.root)

		if (savedInstanceState == null) {
			showPlanListFragment()
		}

		_bind.mainToolbar.inflateMenu(R.menu.plan_list_menu)
		mainOptionMenu = MainOptionMenu(sortDialog, mainDiBind, this, _bind.mainToolbar.menu)
		mainOptionMenu.initItems()
		mainDiBind.optionMenu = mainOptionMenu
	}

	override fun onBackPressed() {
		val stack = mainDiBind.stack
		if (stack.count > 1) {
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