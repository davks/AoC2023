package aoc2015.day2

import java.io.File

fun main() {
    val lines = File("src/main/kotlin/aoc2015/day2/input-test.txt").readLines() // 1586300
    println(doFirst(lines))
    println(doFirst2(lines))
    println(doFirst3(lines))
    println(doFirst4(lines))
    println(doFirst5(lines))
    println(doFirst6(lines))
    doSecond(lines)
}

fun doFirst(lines: List<String>): Int {
    var result = 0
    for (line in lines) {
        val numbers = line.split("x")
        val a = numbers[0].toInt()
        val b = numbers[1].toInt()
        val c = numbers[2].toInt()
        val numbersInt = intArrayOf(a * b, a * c, b * c)
        result += 2 * (a * b + a * c + b * c) + numbersInt.min()
    }

    return result
}

fun doFirst2(lines: List<String>): Int {
    var result = 0
    for (line in lines) {
        val (a, b, c) = line.split("x").map { it.toInt() }
        val numbersInt = intArrayOf(a * b, a * c, b * c)
        result += 2 * numbersInt.sum() + numbersInt.min()
    }
    return result
}

tailrec fun doFirst3(lines: List<String>, result: Int = 0): Int {
    if (lines.isEmpty()) return result

    val (a, b, c) = lines.first().split("x").map { it.toInt() }
    val numbers = intArrayOf(a * b, a * c, b * c)

    return doFirst3(lines.drop(1), result + 2 * numbers.sum() + numbers.min())
}

fun doFirst4(lines: List<String>): Int = lines
    .map { line -> line.split("x").map { it.toInt() } }
    .map { line -> intArrayOf(line[0] * line[1], line[0] * line[2], line[1] * line[2]) }
    .runningFold(0) { _, line -> 2 * line.sum() + line.min() }
    .sum()

fun doFirst5(lines: List<String>): Int = lines
    .map { line -> line.split("x") }
    .map { line ->
        intArrayOf(
            line[0].toInt() * line[1].toInt(),
            line[0].toInt() * line[2].toInt(),
            line[1].toInt() * line[2].toInt()
        )
    }.sumOf { line -> 2 * line.sum() + line.min() }

fun doFirst6(lines: List<String>): Int = lines
    .map { line -> line.split("x").map { it.toInt() } }
    .map { line -> line.let { (a, b, c) -> intArrayOf(a * b, a * c, b * c) } }
    .sumOf { line -> 2 * line.sum() + line.min() }

fun doSecond(text: List<String>) {

}
