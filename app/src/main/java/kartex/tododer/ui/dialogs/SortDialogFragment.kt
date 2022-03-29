package kartex.tododer.ui.dialogs

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.widget.Button
import androidx.fragment.app.DialogFragment
import kartex.tododer.R
import kartex.tododer.ui.sort.SortByTime
import kartex.tododer.ui.sort.SortByTitle
import kartex.tododer.ui.sort.TodoSort

typealias OnCallback = (SortDialogFragment.Companion.SortResult) -> Unit

class SortDialogFragment : DialogFragment {

	// <editor-fold desc="FIELD`S"
	private lateinit var buttonByTitle: Button
	private lateinit var buttonByTime: Button

	private lateinit var items: Array<String>

	private var _result: SortResult = SortResult.Cancel
	private var _callback: OnCallback? = null
	// </editor-fold>

	// <editor-fold desc="PROP`S">
	val result: SortResult
		get() = _result
	// </editor-fold>

	// <editor-fold desc="CTOR`S">
	constructor() : super()

	constructor(layoutRes: Int) : super(layoutRes)
	// </editor-fold>

	fun setCallback(func: OnCallback?) {
		_callback = func
	}

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)

		items = arrayOf(
			getString(R.string.dialog_filter_by_title),
			getString(R.string.dialog_filter_by_time)
		)
	}

	@SuppressLint("DialogFragmentCallbacksDetector")
	override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
		_result = SortResult.Cancel
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