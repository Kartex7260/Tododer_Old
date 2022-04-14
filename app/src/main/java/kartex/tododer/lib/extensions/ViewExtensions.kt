package kartex.tododer.lib.extensions

import android.content.Context
import android.view.View
import android.view.ViewGroup

fun Int.toDpi(context: Context) = (this * context.resources.displayMetrics.density).toInt()

fun Int.toDpiF(context: Context) = this * context.resources.displayMetrics.density

fun Float.toDpi(context: Context) = (this * context.resources.displayMetrics.density).toInt()

fun Float.toDpiF(context: Context) = this * context.resources.displayMetrics.density

fun View.removeFromParent() {
	if (parent == null)
		return
	val parent = parent as ViewGroup
	parent.removeView(this)
}