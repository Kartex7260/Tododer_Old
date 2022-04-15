package kartex.tododer.extensions

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import com.google.android.material.R

private val attrsIds: IntArray = intArrayOf(
	R.attr.colorPrimary,
	R.attr.colorPrimaryVariant,
	R.attr.colorOnPrimary
)
private var colorSet: ColorSet? = null

fun Context.getColorOnPrimary(): Int {
	if (colorSet == null)
		initColorSet()
	return colorSet!!.colorOnPrimary
}

@SuppressLint("ResourceType")
private fun Context.initColorSet() {
	colorSet = ColorSet().also { colorSet ->
		theme.obtainStyledAttributes(attrsIds).apply {
			colorSet.colorPrimary = getColor(0, Color.BLACK)
			colorSet.colorPrimaryVariant = getColor(1, Color.GRAY)
			colorSet.colorOnPrimary = getColor(2, Color.WHITE)
		}.recycle()
	}
}

private class ColorSet {
	var colorPrimary: Int = 0
	var colorPrimaryVariant: Int = 0
	var colorOnPrimary: Int = 0
}