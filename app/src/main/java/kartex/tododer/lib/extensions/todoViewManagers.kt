package kartex.tododer.lib.todo.visitor

import android.content.Context
import kartex.tododer.lib.todo.ITodo
import kartex.tododer.ui.TodoView

private val cardViewVisitors: MutableMap<Context, CardViewVisitor> = HashMap()
private val cardManagers: MutableMap<Context, ViewManagerVisitor> = HashMap()

fun Context.getCardViewManager(): ViewManagerVisitor {
	val cardVisitor = getCardVisitor()
	var manager = cardManagers[this]
	if (manager == null) {
		manager = ViewManagerVisitor(cardVisitor)
		cardManagers[this] = manager
	}
	return manager
}

fun Context.getCardVisitor(): CardViewVisitor {
	var visitor = cardViewVisitors[this]
	if (visitor == null) {
		visitor = CardViewVisitor(this)
		cardViewVisitors[this] = visitor
	}
	return visitor
}

fun ITodo.createCardView(context: Context): TodoView<out ITodo> {
	val cardVisitor = context.getCardVisitor()
	return resultVisit(cardVisitor)
}