package kartex.tododer.lib

import savvy.toolkit.Event
import savvy.toolkit.EventArgs
import java.lang.Exception

open class StateSwitcher<T> {

	private val _eventLocker = Any()

	private val states: MutableMap<String, T> = HashMap()
	private var _state: String = ""

	open var state: String
		get() = _state
		set(value) {
			_state = value

			val args = StateChangeEventArgs(value)
			onStateChange.invoke(_eventLocker, args)
		}

	val onStateChange: Event<StateChangeEventArgs> = Event(_eventLocker)


	fun register(state: String, value: T) {
		if (states.containsKey(state))
			throw Exception("value $state already exist")
		states[state] = value
	}

	fun get() = states[state]

	fun remove(key: String) = states.remove(key)


	companion object {
		class StateChangeEventArgs(val state: String) : EventArgs()
	}
}