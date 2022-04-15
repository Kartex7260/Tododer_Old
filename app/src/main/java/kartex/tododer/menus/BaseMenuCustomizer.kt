package kartex.tododer.menus

import android.view.Menu
import android.view.MenuItem

abstract class BaseMenuCustomizer(val menu: Menu) {

	private val _tagGroups: MutableMap<String, MenuItemsTagGroup> = HashMap()

	abstract fun initItems()

	fun registerTag(tag: String): MenuItemsTagGroup {
		var tagGroup = _tagGroups[tag]
		if (tagGroup == null) {
			tagGroup = MenuItemsTagGroup(tag, menu)
			_tagGroups[tag] = tagGroup
		}
		return tagGroup
	}

	fun doForEvery(tag: String, func: (MenuItem) -> Unit) {
		val tagGroup = _tagGroups[tag] ?: return
		tagGroup.forEach(func)
	}
}