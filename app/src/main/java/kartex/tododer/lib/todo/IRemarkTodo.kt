package kartex.tododer.lib.todo

import android.content.Context
import android.view.View

interface IRemarkTodo : ITodo {

	var remark: String

	override fun clone(): IRemarkTodo
}