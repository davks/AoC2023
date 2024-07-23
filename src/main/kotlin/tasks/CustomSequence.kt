package tasks

typealias Generator<T> = () -> T

fun main() {
    demo()
}

fun demo() {
    val gen = range(1, 14)

//    println(gen())  // 1
//    println(gen())  // 2
//    println(gen())  // 3
//    println(gen())  // NoSuchElementException

    val gen2 = range('a', 'd')
//    println(gen2())
//    println(gen2())
//    println(gen2())
//    println(gen2())

//    forEach(gen2) { println(it) }
//
//    println(first(gen))

    val f1 = filter(gen) { it % 2 == 0 }
    val f2 = filter(f1) { it % 3 == 0 }
//    val v = first(f1)
    forEach(f2) { println(it) }
//    println(v)
}

fun range(start: Int, end: Int): Generator<Int> {
    var i = start

    fun next(): Int {
        if (i >= end) {
            throw NoSuchElementException()
        }
        return i++
    }

    return ::next
}

fun range(start: Char, end: Char): Generator<Char> {
    var i = start

    fun next(): Char {
        if (i >= end) {
            throw NoSuchElementException()
        }
        return i++
    }

    return ::next
}

fun range2(start: Int, end: Int): Generator<Int> {
    var i = start

    return {
        if (i >= end) {
            throw NoSuchElementException()
        }
        i++
    }
}

fun range3(start: Int, end: Int): Generator<Int> {
    var i = start

    return fun(): Int {
        if (i >= end) {
            throw NoSuchElementException()
        }
        return i++
    }
}

fun <T> forEach(gen: Generator<T>, action: (T) -> Unit) {
    while (true) {
        try {
            action(gen())
        } catch (e: NoSuchElementException) {
            break
        }
    }
}

fun <T> first(gen: Generator<T>): T {
    return gen()
}

fun <T> filter(gen: Generator<T>, predicate: (T) -> Boolean): Generator<T> {
    fun next(): T {
        while (true) {
            val value = gen()
            if (predicate(value)) {
                return value
            }
        }
    }

    return ::next
}

