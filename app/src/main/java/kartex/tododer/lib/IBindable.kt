package kartex.tododer.lib

interface IBindable<T> {

	var bind: T?

	fun onWriteToBind(t: T)
	fun onReadFromBind(t: T)
}