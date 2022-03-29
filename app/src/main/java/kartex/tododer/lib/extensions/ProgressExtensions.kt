package kartex.tododer.lib.extensions

import kartex.tododer.lib.todo.*

fun IPlan.computeProgress(): Float {
	progress.clear()
	computeAllProgress(progress)
	return progress.ratio
}

fun IPlan.computeAllProgress(progress: Progress) {
	for (plan in plans) {
		plan.computeAllProgress(progress)
	}
	computeTaskProgress(progress)
}


fun ITreeTodo<ITask>.computeTaskProgress(progress: Progress) {
	for (task in todos) {
		task.checkProgress(progress)
		task.computeTaskProgress(progress)
	}
}

fun ITask.checkProgress(progress: Progress) {
	progress.all++
	if (check) progress.complete++
}

// <editor-fold desc="utils">
fun Progress.cloneTo(progress: Progress) {
	progress.all = all
	progress.complete = complete
}
// </editor-fold>
