package kartex.tododer.lib.model.lazy

import kartex.tododer.lib.extensions.*
import kartex.tododer.lib.todo.IPlan
import kartex.tododer.lib.todo.ITask
import kartex.tododer.lib.todo.Progress
import kartex.tododer.lib.todo.dto.PlanDTO

open class LazyPlan(dir: String, plan: IPlan, dto: PlanDTO) : LazyTreeTodo<ITask, IPlan, PlanDTO>(dir, plan, dto), IPlan {

	override val progress: Progress
		get() = todo.progress

	override val plans: MutableList<IPlan>
		get() = todo.plans

	override fun clone() = todo.clone()

	// <editor-fold desc="nexting">
	override fun toFull(): IPlan {
		val plan = todo.cloneWithoutChildren()
		plan.fillPlans(dir, dto)
		return todo
	}

	open fun nextPlan(index: Int): LazyPlan {
		val cuid = dto.plansCUIDS[index]
		return nextPlan(cuid)
	}

	open fun nextPlan(cuid: String): LazyPlan {
		val dto = readTodo(dir, cuid, ::jsonToPlanDTO)
		val plan = dto.toPlan(todo)
		val lazyPlan = LazyPlan(dir, plan, dto)
		lazyPlan.fillOIDPlansFromCache()
		return lazyPlan
	}

	open fun nextPlans(): List<LazyPlan> {
		val result = ArrayList<LazyPlan>()
		for (cuid in dto.plansCUIDS) {
			val plan = nextPlan(cuid)
			result.add(plan)
		}
		return result
	}

	open fun nextFullPlan(cuid: String): IPlan {
		val lazy = nextPlan(cuid)
		return lazy.toFull()
	}

	open fun nextFullPlans(): List<IPlan> {
		val plans = ArrayList<IPlan>()
		for (cuid in dto.plansCUIDS) {
			val plan = nextFullPlan(cuid)
			plans.add(plan)
		}
		return plans
	}
	// </editor-fold>
}