package aoc2015.day7a2

import java.io.File

// Second version 7A

fun main() {
    val day7 = File("src/main/kotlin/aoc2015/day7/input.txt").readLines()
    println(doFirst(day7.toMutableList()))
    println(doFirst2(day7))
    println(doFirst3(day7))
}

private enum class UnaryOperator {
    NOT, COPY
}

private enum class BinaryOperator {
    AND, OR, RSHIFT, LSHIFT
}

private fun doFirst(
    lines: MutableList<String>,
    foundValues: MutableMap<String, Int> = mutableMapOf()
): Int? {
    fun getSignalValue(s: String) = s.toIntOrNull() ?: foundValues[s]

    while (lines.isNotEmpty()) {
        val itemsToRemove = mutableListOf<String>()

        for (line in lines) {
            val result = getOneOfTheSignal(line).calculate(::getSignalValue)
            result?.let {
                foundValues[result.first] = result.second
                itemsToRemove.add(line)
            }
        }
        lines.removeAll(itemsToRemove)
    }

    return foundValues["a"]
}

private fun doFirst2(
    lines: List<String>,
    foundValues: MutableMap<String, Int> = mutableMapOf()
): Int? {
    fun getSignalValue(s: String) = s.toIntOrNull() ?: foundValues[s]

    // Toto je zajímavé. Vytvoří seznam naparsovaných objektů dopředu
    var signals = lines.map(::getOneOfTheSignal)

    while (signals.isNotEmpty()) {
        val newSignals = mutableListOf<Signal>() // Vytvoříme prázdný seznam

        for (signal in signals) {
            val result = signal.calculate(::getSignalValue)
            if (result != null) {
                foundValues[result.first] = result.second
            } else {
                newSignals.add(signal) // vkládáme nespočítané signály
            }
        }
        signals = newSignals // seznam s nevypočítanými signály se použije pro další iteraci
    }
    return foundValues["a"]
}

private fun doFirst3(lines: List<String>): Int? {
    val signals = lines.map(::getOneOfTheSignal)
    return doFirst3(signals)
}

private tailrec fun doFirst3(signals: List<Signal>, foundValues: MutableMap<String, Int> = mutableMapOf()): Int? {
    if (signals.isEmpty()) return foundValues["a"]

    val newSignals = mutableListOf<Signal>()

    for (signal in signals) {
        val result = signal.calculate { s -> s.toIntOrNull() ?: foundValues[s]}
        if (result != null) {
            foundValues[result.first] = result.second
        } else {
            newSignals.add(signal) // vkládáme nespočítané signály
        }
    }

    return doFirst3(newSignals, foundValues)
}

private sealed class Signal {
    abstract fun calculate(foundSignals: (String) -> Int?): Pair<String, Int>?
}

private data class BinarySignal(val valueA: String, val operator: BinaryOperator, val valueB: String, val result: String) :
    Signal() {
    override fun calculate(foundSignals: (String) -> Int?): Pair<String, Int>? {
        val valueA = foundSignals(this.valueA)
        val valueB = foundSignals(this.valueB)
        if (valueA == null || valueB == null) {
            return null
        }
        return this.result to when (this.operator) {
            BinaryOperator.AND -> valueA and valueB
            BinaryOperator.OR -> valueA or valueB
            BinaryOperator.LSHIFT -> valueA shl valueB
            BinaryOperator.RSHIFT -> valueA shr valueB
        }
    }
}

// Dej mi svoji hodnotu pokud můžeš (dávám, co znám) vs dej mi svoji hodnotu a pokud máš nějaký závislosti, tak si je zjisti
private data class UnarySignal(val operator: UnaryOperator,val value: String, val result: String) : Signal() {
    override fun calculate(foundSignals: (String) -> Int?): Pair<String, Int>? {
        return when (operator) {
            UnaryOperator.COPY -> foundSignals(this.value)?.let { this.result to it }
            UnaryOperator.NOT -> foundSignals(this.value)?.let { this.result to 65536 + it.inv() }
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
