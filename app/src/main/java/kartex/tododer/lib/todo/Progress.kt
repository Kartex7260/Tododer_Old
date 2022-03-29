package kartex.tododer.lib.todo

class Progress(var all: Int = 0, var complete: Int = 0) {

	val ratio: Float
		get() = complete.toFloat() / all.toFloat()

	val ratioPercent
		get() = ratio * 100

	operator fun plusAssign(progress: Progress) {
		all += progress.all
		complete += progress.complete
	}

	fun clear() {
		all = 0
		complete = 0
	}

}