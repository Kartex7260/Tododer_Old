package kartex.tododer.lib.todo.dto.oid

import kartex.tododer.lib.todo.dto.IPlansParent
import kartex.tododer.lib.todo.dto.ITodosParent

open class OIDPlanDTO : OIDTreeTodoDTO(), IPlansParent, ITodosParent {

	override var plansCUIDS: MutableList<String> = ArrayList()
}