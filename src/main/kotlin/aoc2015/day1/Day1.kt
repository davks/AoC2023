package aoc2015.day1

import java.io.File
import kotlin.math.floor

fun main() {
    val day1 = File("src/main/kotlin/aoc2015/day1/input-test.txt").readText()
    //println(doFirst(day1))
//    println(doFirst2(day1))
//    println(doSecond(day1, 0, 0))
    println(doSecond3(day1))

    println(day1.length - 2 * day1.count { it == ')' })
    println(day1.length)
}

fun doFirst(text: String): Int {
    var floor = 0
    val line = text.toCharArray()
    line.forEach { oneChar ->
        if (oneChar == '(') floor++
        if (oneChar == ')') floor--
    }
    return floor
}

fun doFirst(text: String, index: Int, floor: Int): Int {
    if (index >= text.length) return floor

    if (text[index] == '(') return doFirst(text, index + 1, floor + 1)
    if (text[index] == ')') return doFirst(text, index + 1, floor - 1)

    return doFirst(text, index + 1, floor)
}

tailrec fun doFirst(text: String, floor: Int): Int {
    if (text.isEmpty()) return floor

    if (text[0] == '(') return doFirst(text.substring(1), floor + 1)
    if (text[0] == ')') return doFirst(text.substring(1), floor - 1)

    return doFirst(text.substring(1), floor)
}

tailrec fun doFirst2(text: String, floor: Int): Int {
    if (text.isEmpty()) return floor

    return doFirst2(text.substring(1), if (text[0] == '(') floor + 1 else floor - 1)
}

fun doFirst2(text: String): Int {
    return text
        .fold(0) { floor, oneChar -> if (oneChar == '(') floor + 1 else floor - 1 }
}

fun doSecond(text: String): Int {
    var floor = 0

    for ((index, oneChar) in text.withIndex()) {
        if (oneChar == '(') floor++ else floor--
        if (floor == -1) return index + 1
    }

    return -1
}

tailrec fun doSecond(text: String, floor: Int, index: Int): Int {
    if (floor == -1) return index
    if (text.isEmpty()) return -1

    return doSecond(text.substring(1), if(text[0] == '(') floor + 1 else floor - 1 , index + 1)
}

fun doSecond2(text: String): Int {
    return text.asSequence()
        .runningFold(0) { floor, oneChar -> if (oneChar == '(') floor + 1 else floor - 1}
        .takeUntil { it == -1 }
        .count()
}

fun <T> Sequence<T>.takeUntil(predicate: (T) -> Boolean): Sequence<T> = sequence {
    for (item in this@takeUntil) {
        if (predicate(item)) break
        yield(item)
    }
}

fun doSecond3(text: String): Int {
    return text.asSequence()
        .runningFold(0) { floor, oneChar -> if (oneChar == '(') floor + 1 else floor - 1}
        .indexOfFirst { it == -1 }
}