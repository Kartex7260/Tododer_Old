package kartex.tododer.ui.dialogs

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.widget.Button
import androidx.core.os.bundleOf
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.setFragmentResult
import kartex.tododer.R
import kartex.tododer.ui.sort.SortByTime
import kartex.tododer.ui.sort.SortByTitle
import kartex.tododer.ui.sort.TodoSort

class SortDialogFragment : DialogFragment {

	// <editor-fold desc="FIELD">
	private var _callback: OnCallback<SortResult>? = null
	// </editor-fold>


	// <editor-fold desc="CTOR`S">
	constructor() : super()

	constructor(layoutRes: Int) : super(layoutRes)
	// </editor-fold>

	fun setCallback(func: OnCallback<SortResult>?) {
		_callback = func
	}

	@SuppressLint("DialogFragmentCallbacksDetector")
	override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
		return activity?.let {
			val builder = AlertDialog.Builder(context)
			builder.setTitle(R.string.dialog_filter_title)
			builder.setItems(R.array.dialogFilterItems
			) { _, which ->
				when (which) {
					0 -> _callback?.invoke(SortResult.ByTitle)
					1 -> _callback?.invoke(SortResult.ByTime)
				}
			}
			builder.setNegativeButton(R.string.cancel, null)
			builder.create()
		} ?: throw Exception("Dialog is null")
	}

	companion object {

		fun getFromParameters(id: Int, reverse: Boolean): TodoSort? {
			val sortResult = SortResult.values().find { it.sortObj?.id == id } ?: return null

			return if (reverse)
				sortResult.sortObjReverse
			else
				sortResult.sortObj
		}

		enum class SortResult(val sortObj: TodoSort? = null, val sortObjReverse: TodoSort? = null) {
			ByTitle(SortByTitle.INST, SortByTitle.INST_REVERSE),
			ByTime(SortByTime.INST, SortByTime.INST_REVERSE),
			Cancel;

			fun toTodoSort(reverse: Boolean = false): TodoSort? {
				if (reverse)
					return sortObjReverse
				return sortObj
			}
		}
	}
}