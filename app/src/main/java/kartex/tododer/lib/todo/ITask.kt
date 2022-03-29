package kartex.tododer.lib.todo

interface ITask : ITreeTodo<ITask> {
	var check: Boolean

	override fun clone(): ITask
}