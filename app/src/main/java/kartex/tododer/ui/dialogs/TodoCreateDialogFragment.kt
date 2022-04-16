package kartex.tododer.ui.dialogs

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import kartex.tododer.R
import kartex.tododer.lib.extensions.createNewTask
import kartex.tododer.lib.model.IEventTodoDB
import kartex.tododer.lib.todo.ITask
import kartex.tododer.lib.todo.ITodo
import kartex.tododer.lib.todo.ITreeTodo

class TodoCreateDialogFragment : DialogFragment {

	// <editor-fold desc="FIELD`S">
	private var _callback: OnCallback<Int>? = null
	// </editor-fold>

	// <editor-fold desc="CTOR`S">
	constructor() : super()

	constructor(layoutRes: Int) : super(layoutRes)
	// </editor-fold>

	fun setCallback(callback: OnCallback<Int>?) {
		_callback = callback
	}

	@SuppressLint("DialogFragmentCallbacksDetector")
	override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
		return activity?.let {
			val builder = AlertDialog.Builder(context)
			builder.setTitle(R.string.dialog_todo_create_title)
			builder.setItems(
				R.array.dialogTodoCreateItem
			) { _, which ->
				when (which) {
					0 -> _callback?.invoke(CREATE_TASK)
					1 -> _callback?.invoke(CREATE_PLAN)
				}
			}
			builder.setNegativeButton(R.string.cancel, null)
			builder.create()
		} ?: throw Exception("Dialog is null")
	}

	companion object {

		const val CREATE_TASK: Int = 0
		const val CREATE_PLAN: Int = 1
	}
}