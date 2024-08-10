package aoc2015.day7a1

import java.io.File

// First version 7A

fun main() {
    val day7 = File("src/main/kotlin/aoc2015/day7/input.txt").readLines().toMutableList()
    println(doFirst(day7))
}

private enum class Operators {
    AND, OR, RSHIFT, LSHIFT
}

private sealed class Signal

private data class SignalOne(val valueA: String, val operator: Operators, val valueB: String, val result: String): Signal()
private data class SignalTwo(val value: String, val result: String): Signal()
private data class SignalThree(val operator: String, val value: String, val result: String): Signal()

private fun doFirst(
    lines: MutableList<String>,
    foundValues: MutableMap<String, Int> = mutableMapOf()
): Int? {
    while (lines.isNotEmpty()) {
        val itemsToRemove = mutableListOf<String>()

        for (line in lines) {
            val isCalculated = when (val signal = getOneOfTheSignal(line)) {
                is SignalOne -> calculateSignalAndModify(signal, foundValues)
                is SignalTwo -> calculateSignalAndModify(signal, foundValues)
                is SignalThree -> calculateSignalAndModify(signal, foundValues)
                null -> throw IllegalArgumentException("Unknown type: $line")
            }
            if (isCalculated) {
                itemsToRemove.add(line)
            }
        }
        lines.removeAll(itemsToRemove)
    }

    return foundValues["a"]
}

private fun getOneOfTheSignal(line: String): Signal? {
    val regex1 = "^(.{1,3}) (AND|OR|RSHIFT|LSHIFT) (.{1,3}) -> (.{1,3})".toRegex()
    val regex2 = "^(\\w{1,5}) -> (.{1,3})".toRegex()
    val regex3 = "^(NOT) (.{1,3}) -> (.{1,3})".toRegex()

    fun getSignalOne(matchResult: MatchResult): SignalOne? {
        val (a, operator, b, result) = matchResult.destructured
        val eOperator = when (operator) {
            "AND" -> Operators.AND
            "OR" -> Operators.OR
            "LSHIFT" -> Operators.LSHIFT
            "RSHIFT" -> Operators.RSHIFT
            else -> throw IllegalArgumentException("Unknown operator")
        }
        return SignalOne(a, eOperator, b, result)
    }

    fun getSignalTwo(matchResult: MatchResult): SignalTwo? {
        val (a, result) = matchResult.destructured
        return SignalTwo(a, result)
    }

    fun getSignalThree(matchResult: MatchResult): SignalThree? {
        val (operator, b, result) = matchResult.destructured
        return SignalThree(operator, b, result)
    }

    var signal: Signal? = getSignal(regex1, line, ::getSignalOne)
    signal = signal ?: getSignal(regex2, line, ::getSignalTwo)
    signal = signal ?: getSignal(regex3, line, ::getSignalThree)

    return signal
}

private fun <T : Signal> getSignal(regex: Regex, line: String, get: (MatchResult) -> T?): T? {
    val matchResult = regex.find(line)

    return if (matchResult != null) {
        get(matchResult)
    } else {
        null
    }
}

private fun calculateSignalAndModify(signal: SignalOne, foundValues: MutableMap<String, Int>): Boolean {
    val valueA = signal.valueA.toIntOrNull() ?: foundValues[signal.valueA]
    val valueB = signal.valueB.toIntOrNull() ?: foundValues[signal.valueB]

    return if (valueA == null || valueB == null){
        false
    } else {
        val result = when (signal.operator) {
            Operators.AND -> valueA and valueB
            Operators.OR -> valueA or valueB
            Operators.LSHIFT -> valueA shl valueB
            Operators.RSHIFT -> valueA shr valueB
        }
        foundValues[signal.result] = result
        true
    }
}

private fun calculateSignalAndModify(signal: SignalTwo, foundValues: MutableMap<String, Int>): Boolean {
    val value = signal.value.toIntOrNull() ?: foundValues[signal.value]

    return if (value == null) {
        false
    } else {
        val result: Int = value
        foundValues[signal.result] = result
        true
    }
}

private fun calculateSignalAndModify(signal: SignalThree, foundValues: MutableMap<String, Int>): Boolean {
    val value = signal.value.toIntOrNull() ?: foundValues[signal.value]

    return if (value == null) {
        false
    } else {
        val result = 65536 + value.inv()
        foundValues[signal.result] = result
        true
    }
}
