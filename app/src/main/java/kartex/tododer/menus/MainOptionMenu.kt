package kartex.tododer.menus

import android.annotation.SuppressLint
import android.view.Menu
import androidx.fragment.app.FragmentActivity
import kartex.tododer.R
import kartex.tododer.extensions.getColorOnPrimary
import kartex.tododer.lib.MainDIBind
import kartex.tododer.ui.dialogs.SortDialogFragment

class MainOptionMenu(val sortDialog: SortDialogFragment, val mainDiBind: MainDIBind, val activity: FragmentActivity, menu: Menu) : BaseMenuCustomizer(menu) {

	init {
		registerTag(PLAN_LIST_GROUP).apply {
			add(R.id.menuReverse)
			add(R.id.menuSort)
		}
	}

	fun visiblePlanGroup(visible: Boolean) {
		doForEvery(PLAN_LIST_GROUP) {
			it.isVisible = visible
		}
	}

	override fun initItems() {
		menu.apply {
			val colorOnPrimary = activity.getColorOnPrimary()
			initItemSettings()
			initItemReverse(colorOnPrimary)
			initItemSort(colorOnPrimary)
		}
	}

	private fun initItemSettings() {
		menu.findItem(R.id.menuSettings).apply {
			setOnMenuItemClickListener {
				/*val intent = Intent(activity, SettingsActivity::class.java)
				activity.startActivity(intent)*/
				return@setOnMenuItemClickListener true
			}
		}
	}

	@SuppressLint("UseCompatLoadingForDrawables")
	private fun initItemReverse(colorOnPrimary: Int) {
		menu.findItem(R.id.menuReverse).apply {
			val iconNormal = activity.getDrawable(R.drawable.sort_reverse_24)?.apply {
				setTint(colorOnPrimary)
			}
			val iconReverse = activity.getDrawable(R.drawable.sort_reverse_24_rev)?.apply {
				setTint(colorOnPrimary)
			}

			fun updateIcon() {
				icon = if (mainDiBind.sort.reverse)
					iconReverse
				else
					iconNormal
			}

			updateIcon()

			setOnMenuItemClickListener {
				mainDiBind.sort = mainDiBind.sort.asReverse()
				updateIcon()
				return@setOnMenuItemClickListener true
			}
		}
	}

	@SuppressLint("UseCompatLoadingForDrawables")
	private fun initItemSort(colorOnPrimary: Int) {
		menu.findItem(R.id.menuSort).apply {
			icon = activity.getDrawable(mainDiBind.sort.iconResId)?.apply { setTint(colorOnPrimary) }
			setOnMenuItemClickListener {
				sortDialog.setCallback {
					val sortResult = it.toTodoSort(mainDiBind.sort.reverse) ?: return@setCallback
					mainDiBind.sort = sortResult
					icon = activity.getDrawable(sortResult.iconResId)?.apply { setTint(colorOnPrimary) }
				}
				sortDialog.show(activity.supportFragmentManager, null)
				return@setOnMenuItemClickListener true
			}
		}
	}

	companion object {

		const val PLAN_LIST_GROUP = "plan_list_state"
	}
}