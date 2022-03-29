package kartex.tododer.lib.extensions

fun <T> Iterable<T>.contains(func: (T) -> Boolean): Boolean {
	for (t in this) {
		if (func(t))
			return true
	}
	return false
}

fun <T> Array<T>.contains(func: (T) -> Boolean): Boolean {
	for (t in this) {
		if (func(t))
			return true
	}
	return false
}