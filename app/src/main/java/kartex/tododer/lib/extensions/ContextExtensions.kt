package kartex.tododer.lib.extensions

import android.content.Context
import android.view.View
import kartex.tododer.lib.todo.visitor.ITodoResultVisitor
import kartex.tododer.lib.todo.visitor.ViewManagerVisitor

private val managers: Map<Context, ViewManagerVisitor> = HashMap()

fun Context.getViewManager(viewVisitor: ITodoResultVisitor<out View>): ViewManagerVisitor {
	var manager = managers[this]
	if (manager == null) {
		manager = ViewManagerVisitor(viewVisitor)
	}
	return manager
}