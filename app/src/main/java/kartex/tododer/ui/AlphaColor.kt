package kartex.tododer.ui

import android.graphics.Color

class AlphaColor(intColor: Int) {

	var alpha: Float
	val red: Float
	val green: Float
	val blue: Float

	init {
		val color = Color.valueOf(intColor)
		alpha = color.alpha()
		red = color.red()
		green = color.green()
		blue = color.blue()
	}

	fun computeWithRatio(ratio: Float): Int {
		val newAlpha = alpha * ratio
		return toIntColor(newAlpha)
	}

	fun toIntColor() = toIntColor(alpha)

	fun toIntColor(alpha: Float): Int {
		return (alpha * 255.0f + 0.5f).toInt() shl 24 or
				((red * 255.0f + 0.5f).toInt() shl 16) or
				((green * 255.0f + 0.5f).toInt() shl 8) or
				(blue * 255.0f + 0.5f).toInt()
	}
}