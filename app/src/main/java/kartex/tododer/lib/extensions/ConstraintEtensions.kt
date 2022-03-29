package kartex.tododer.lib.extensions

import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import androidx.transition.TransitionManager

private val set = ConstraintSet()

fun ConstraintLayout.constraintSet(func: ConstraintSet.() -> Unit) {
	set.clone(this)
	func(set)
	TransitionManager.beginDelayedTransition(this)
	set.applyTo(this)
}