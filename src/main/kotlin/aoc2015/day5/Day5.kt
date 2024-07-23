package aoc2015.day5

import java.io.File

fun main() {
    val day5 = File("src/main/kotlin/aoc2015/day5/input.txt").readLines()
    println(doFirst(day5))
    println(doSecond(day5))

}

private fun doFirst(lines: List<String>): Int {
    val doubleLetter = arrayOf("ab", "cd", "pq", "xy")
    val vowels = "aeiou".toCharArray()

    var result = 0

    for (line in lines) {
        if (doubleLetter.asSequence().any { it in line }) continue
        if (line.asSequence().count { it in vowels } < 3) continue
        if (!line.asSequence().zipWithNext().any { it.first == it.second }) continue
        result++
    }

    return result
}

private fun doSecond(lines: List<String>): Int = lines
    .count { line ->
        line.contains("([a-z][a-z]).*\\1".toRegex()) && line.contains("([a-z])[a-z]\\1".toRegex())
    }

private fun hasDoubleLetter(text: String): Boolean {
    for ((index, char) in text.withIndex()) {
        if (index == text.length - 1) break
        if (char == text[index + 1]) return true
    }
    return false
}
