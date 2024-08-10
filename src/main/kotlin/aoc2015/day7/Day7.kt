package aoc2015.day7

import java.io.File

// Last version 7A

fun main() {
    val day7 = File("src/main/kotlin/aoc2015/day7/input.txt").readLines()
    println(doFirst(day7))
    println(doFirst2(day7))
}

private enum class UnaryOperator {
    NOT, COPY
}

private enum class BinaryOperator {
    AND, OR, RSHIFT, LSHIFT
}

private fun doFirst(lines: List<String>): Int {
    val signals = lines.map { getOneOfTheSignal(it) }.associate {
        when (it) {
            is BinarySignal -> it.name to it
            is UnarySignal -> it.name to it
        }
    }
    return calc("a", signals, mutableMapOf())
}

private fun doFirst2(lines: List<String>): Int {
    val signals: Map<String, Signal> = lines.map(::getOneOfTheSignal).associateBy { it.name }
    return calc("a", signals, mutableMapOf())
}

/**
 * foundSignals - cache with values. key is s name of the signal and value is calculate number.
 */
private fun calc(signalName: String, signals: Map<String, Signal>, foundSignals: MutableMap<String, Int>): Int {
    if (signalName.toIntOrNull() != null) return signalName.toInt()
    if (foundSignals.containsKey(signalName)) return foundSignals[signalName]!! // return value from cache

    val result = signals[signalName]!!.calculate(signals, foundSignals) // call calculate - recursion is inside
    foundSignals[signalName] = result // calculate value insert to cache

    return result
}

private sealed class Signal {
    abstract val name: String // name of the signal
    abstract fun calculate(
        signals: Map<String, Signal>,
        foundSignals: MutableMap<String, Int>
    ): Int // calculate the signal
}

private data class BinarySignal(
    val valueA: String,
    val operator: BinaryOperator,
    val valueB: String,
    override val name: String
) : Signal() {
    override fun calculate(signals: Map<String, Signal>, foundSignals: MutableMap<String, Int>): Int {
        val valueA = calc(valueA, signals, foundSignals)
        val valueB = calc(valueB, signals, foundSignals)

        return when (this.operator) {
            BinaryOperator.AND -> valueA and valueB
            BinaryOperator.OR -> valueA or valueB
            BinaryOperator.LSHIFT -> valueA shl valueB
            BinaryOperator.RSHIFT -> valueA shr valueB
        }
    }
}

// Dej mi svoji hodnotu pokud můžeš (dávám, co znám) vs dej mi svoji hodnotu a pokud máš nějaký závislosti, tak si je zjisti
private data class UnarySignal(val operator: UnaryOperator, val value: String, override val name: String) : Signal() {
    override fun calculate(signals: Map<String, Signal>, foundSignals: MutableMap<String, Int>): Int {
        val value = calc(value, signals, foundSignals)

        return when (operator) {
            UnaryOperator.COPY -> value
            UnaryOperator.NOT -> 65536 + value.inv()
        }
    }
}

private fun getOneOfTheSignal(line: String): Signal {
    val regex1 = "^(\\w{1,3}) (AND|OR|RSHIFT|LSHIFT) (\\w{1,3}) -> (\\w{1,3})".toRegex()
    val regex2 = "^(NOT )?(\\w{1,5}) -> (\\w{1,3})".toRegex()

    fun getBinarySignal(matchResult: MatchResult): BinarySignal? {
        val (a, operator, b, result) = matchResult.destructured
        val eOperator = when (operator) {
            "AND" -> BinaryOperator.AND
            "OR" -> BinaryOperator.OR
            "LSHIFT" -> BinaryOperator.LSHIFT
            "RSHIFT" -> BinaryOperator.RSHIFT
            else -> throw IllegalArgumentException("Unknown operator")
        }
        return BinarySignal(a, eOperator, b, result)
    }

    fun getUnarySignal(matchResult: MatchResult): UnarySignal? {
        val (operator, a, result) = matchResult.destructured
        val eOperator = when (operator.trim()) {
            "NOT" -> UnaryOperator.NOT
            else -> UnaryOperator.COPY
        }
        return UnarySignal(eOperator, a, result)
    }

    var signal: Signal? = getSignal(regex1, line, ::getBinarySignal)
    signal = signal ?: getSignal(regex2, line, ::getUnarySignal)

    return signal!!
}

private fun <T : Signal> getSignal(regex: Regex, line: String, get: (MatchResult) -> T?): T? {
    val matchResult = regex.find(line)

    return if (matchResult != null) {
        get(matchResult)
    } else {
        null
    }
}
