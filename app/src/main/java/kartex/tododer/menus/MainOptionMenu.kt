package kartex.tododer.menus

import android.annotation.SuppressLint
import android.graphics.drawable.Drawable
import android.view.Menu
import androidx.fragment.app.FragmentActivity
import kartex.tododer.R
import kartex.tododer.extensions.getColorOnPrimary
import kartex.tododer.lib.ISortable
import kartex.tododer.lib.MainDIBind
import kartex.tododer.ui.dialogs.SortDialogFragment
import kartex.tododer.ui.sort.getDrawable

class MainOptionMenu(val sortDialog: SortDialogFragment, val activity: FragmentActivity, menu: Menu) : BaseMenuCustomizer(menu) {

	var sortable: ISortable? = null

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

	fun resume() {
		if (sortable == null)
			return
		menu.findItem(R.id.menuSort).apply {
			icon = sortable!!.sort.getDrawable(activity).colorOnPrimary()
		}
		updateIconReverse()
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
	private fun updateIconReverse() {
		val iconNormal = activity.getDrawable(R.drawable.toolbar_sort_reverse_24)
		val iconReverse = activity.getDrawable(R.drawable.toolbar_sort_reverse_24_rev)
		sortable?.apply {
			menu.findItem(R.id.menuReverse).icon = if (sort.reverse)
				iconReverse
			else
				iconNormal
		}
	}

	@SuppressLint("UseCompatLoadingForDrawables")
	private fun initItemReverse() {
		menu.findItem(R.id.menuReverse).apply {
			updateIconReverse()

			setOnMenuItemClickListener {
				it.isEnabled = false
				sortable?.apply {
					sort = sort.asReverse()
				}
				updateIconReverse()
				it.isEnabled = true
				return@setOnMenuItemClickListener true
			}
		}
	}

	@SuppressLint("UseCompatLoadingForDrawables")
	private fun initItemSort() {
		menu.findItem(R.id.menuSort).apply {
			sortable?.apply {
				icon = sort.getDrawable(activity).colorOnPrimary()
			}
			setOnMenuItemClickListener { item ->
				sortDialog.setCallback {
					if (sortable == null)
						return@setCallback
					val sortResult = it.toTodoSort(sortable!!.sort.reverse) ?: return@setCallback
					sortable!!.sort = sortResult
					icon = sortResult.getDrawable(activity).colorOnPrimary()
				}
				item.isEnabled = false
				sortDialog.show(activity.supportFragmentManager, null)
				item.isEnabled = true
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