package kartex.tododer.fragments

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import kartex.tododer.R
import kartex.tododer.lib.Const
import kartex.tododer.lib.model.EventTodoDB
import kartex.tododer.lib.model.IEventTodoDB
import kartex.tododer.lib.model.ITodoDB
import kartex.tododer.lib.model.TodoDBEventArgs
import kartex.tododer.lib.todo.IPlan
import kartex.tododer.lib.todo.ITodo
import kartex.tododer.ui.TodoListView
import org.kodein.di.DI
import org.kodein.di.DIAware
import org.kodein.di.android.closestDI
import org.kodein.di.instance

class PlanListFragment : Fragment(R.layout.fragment_todo_list), DIAware {

	// <editor-fold desc="FIELD`S">
	override val di: DI by closestDI { requireContext() }
	private val db: IEventTodoDB<IPlan> by instance(Const.DITags.DB_MAIN)

	private lateinit var _root: TodoListView<IPlan, IEventTodoDB<IPlan>>
	// </editor-fold>

	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)

		_root = view.findViewById(R.id.fragmentTodoList)
		_root.bind = db
	}

	// <editor-fold desc="PRIVATE">
	// </editor-fold>
}