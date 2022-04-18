package kartex.tododer.menus

import android.annotation.SuppressLint
import android.graphics.drawable.Drawable
import android.view.Menu
import androidx.fragment.app.FragmentActivity
import kartex.tododer.R
import kartex.tododer.extensions.getColorOnPrimary
import kartex.tododer.lib.MainDIBind
import kartex.tododer.ui.dialogs.SortDialogFragment
import kartex.tododer.ui.sort.getDrawable

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

	fun fixColor() {
		menu.findItem(R.id.menuSort).apply {
			icon = mainDiBind.sort.getDrawable(activity).colorOnPrimary()
		}
	}

	override fun initItems() {
		menu.apply {
			initItemSettings()
			initItemReverse()
			initItemSort()
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
	private fun initItemReverse() {
		menu.findItem(R.id.menuReverse).apply {
			val iconNormal = activity.getDrawable(R.drawable.toolbar_sort_reverse_24)
			val iconReverse = activity.getDrawable(R.drawable.toolbar_sort_reverse_24_rev)

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
	private fun initItemSort() {
		menu.findItem(R.id.menuSort).apply {
			icon = mainDiBind.sort.getDrawable(activity).colorOnPrimary()
			setOnMenuItemClickListener {
				sortDialog.setCallback {
					val sortResult = it.toTodoSort(mainDiBind.sort.reverse) ?: return@setCallback
					mainDiBind.sort = sortResult
					icon = sortResult.getDrawable(activity).colorOnPrimary()
				}
				sortDialog.show(activity.supportFragmentManager, null)
				return@setOnMenuItemClickListener true
			}
		}
	}

	private fun Drawable.colorOnPrimary(): Drawable {
		val color = activity.getColorOnPrimary()
		setTint(color)
		return this
	}

	companion object {

		const val PLAN_LIST_GROUP = "plan_list_state"
	}
}