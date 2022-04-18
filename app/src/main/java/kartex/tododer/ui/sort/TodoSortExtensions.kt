package kartex.tododer.ui.sort

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.drawable.Drawable
import androidx.annotation.DrawableRes

private val drawables: MutableMap<TodoSort, Drawable> = HashMap()

fun TodoSort.getDrawable(context: Context): Drawable {
	return drawables[this] ?: createDrawable(this, iconResId, context)
}

@SuppressLint("UseCompatLoadingForDrawables")
private fun createDrawable(todoSort: TodoSort, @DrawableRes id: Int, context: Context): Drawable {
	val drawable = context.getDrawable(id)!!
	drawables[todoSort] = drawable
	return drawable
}