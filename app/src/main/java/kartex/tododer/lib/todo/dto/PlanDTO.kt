package kartex.tododer.lib.todo.dto

class PlanDTO : TreeTodoDTO(), IPlansParent {
	
	override var plansCUIDS: MutableList<String> = ArrayList()
}