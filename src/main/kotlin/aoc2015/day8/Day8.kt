package aoc2015.day8

import java.io.File

fun main() {
    val day8 = File("src/main/kotlin/aoc2015/day8/input.txt").readLines() // 1333
    println(doFirst(day8))
    println(doSecond(day8))
}

private fun doFirst(lines: List<String>): Int {
    val regex = "\\\\x[0-9a-f]{2}".toRegex()

    val codeLength = lines.sumOf { it.length }
    val stringLength = lines.sumOf { line ->
        line
            .replace("\\\\", "-")
            .replace("\\\"", "-")
            .replace(regex, "-")
            .replace("\"", "")
            .length
    }

    return codeLength - stringLength
}

private fun doSecond(lines: List<String>): Int {
    val codeLength = lines.sumOf { it.length }
    val encodesStringLength = lines.sumOf { line ->
        line
            .replace("\\", "\\\\")
            .replace("\"", "\\\"")
            .length + 2
    }

    return encodesStringLength - codeLength
}