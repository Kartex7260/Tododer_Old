package kartex.tododer.menus

import android.view.Menu
import android.view.MenuItem

class MenuItemsTagGroup(val tag: String, private val menu: Menu) : Iterable<MenuItem> {

	private val _items: MutableList<MenuItem> = ArrayList()

	val count: Int
		get() = _items.count()

	fun add(id: Int) {
		val item = menu.findItem(id)
		_items.add(item)
	}

	fun get(id: Int): MenuItem? {
		return _items.firstOrNull { it.itemId == id }
	}

	fun forEach(cur: (MenuItem) -> Unit) {
		_items.forEach(cur)
	}

	override fun iterator(): Iterator<MenuItem> {
		return _items.iterator()
	}
}