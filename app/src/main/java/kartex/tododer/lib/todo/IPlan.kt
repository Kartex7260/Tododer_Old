package kartex.tododer.lib.todo

interface IPlan : ITreeTodo<ITask> {
	val progress: Progress
	val plans: MutableList<IPlan>

	override fun clone(): IPlan
}