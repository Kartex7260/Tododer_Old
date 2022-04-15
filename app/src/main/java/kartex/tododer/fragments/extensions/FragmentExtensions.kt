package kartex.tododer.fragments.extensions

import android.app.ActionBar
import androidx.fragment.app.Fragment

fun Fragment.actionBar(func: ActionBar.() -> Unit) {
	activity?.also { activity ->
		activity.actionBar?.also(func)
	}
}