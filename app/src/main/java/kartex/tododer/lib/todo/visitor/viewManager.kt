package kartex.tododer.lib.todo.visitor

import android.content.Context
import android.view.View

private val managers: Map<Context, ViewManagerVisitor> = HashMap()

fun Context.getViewManager(viewVisitor: ITodoResultVisitor<out View>): ViewManagerVisitor {
	var manager = managers[this]
	if (manager == null) {
		manager = ViewManagerVisitor(viewVisitor)
	} else
		manager.viewVisitor = viewVisitor
	return manager
}