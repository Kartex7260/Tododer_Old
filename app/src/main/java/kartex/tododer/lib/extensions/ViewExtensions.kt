package kartex.tododer.lib.extensions

import android.content.Context

fun Int.toDpi(context: Context) = (this * context.resources.displayMetrics.density).toInt()

fun Int.toDpiF(context: Context) = this * context.resources.displayMetrics.density

fun Float.toDpi(context: Context) = (this * context.resources.displayMetrics.density).toInt()

fun Float.toDpiF(context: Context) = this * context.resources.displayMetrics.density