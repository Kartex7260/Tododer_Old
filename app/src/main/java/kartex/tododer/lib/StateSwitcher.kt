package kartex.tododer.lib

import savvy.toolkit.Event
import savvy.toolkit.EventArgs
import java.lang.Exception

open class StateSwitcher<T> {

	private val _eventLocker = Any()

	private val _states: MutableMap<String, T> = HashMap()
	private var _state: String = ""

	open var state: String
		get() = _state
		set(value) {
			_state = value

			val args = StateChangeEventArgs(value)
			onStateChange.invoke(_eventLocker, args)
		}

	val onStateChange: Event<StateChangeEventArgs> = Event(_eventLocker)

	val states: Map<String, T>
		get() = _states

	fun register(state: String, value: T) {
		if (_states.containsKey(state))
			throw Exception("value $state already exist")
		_states[state] = value
	}

	fun get() = _states[state]

	fun remove(key: String) = _states.remove(key)


	companion object {
		class StateChangeEventArgs(val state: String) : EventArgs()
	}
}