package aoc2023.day4

import aoc2023.day3.loadFile
import kotlin.math.pow

val puzzleInput = loadFile("src/main/kotlin/aoc2023/day4/test-input.txt")

fun main() {
    doFirst(puzzleInput)
    doSecond(puzzleInput)
}

fun doFirst(puzzleInput: List<String>) {
    val result = mutableListOf<Int>()
    puzzleInput.forEach {row ->
        val (left, right) = row.split(":")[1].split("|")

        val leftNumbers = "(\\d+)".toRegex().findAll(left).map { it.value }.toList()
        val rightNumbers = "(\\d+)".toRegex().findAll(right).map { it.value }.toList()

        val foundNumbers = leftNumbers.filter { it in rightNumbers }
        result.add(2.0.pow(foundNumbers.count() - 1).toInt())
    }
    println(result.sumOf { it })
}

fun doSecond(puzzleInput: List<String>) {
    val totalCount = mutableMapOf<Int, Int>()
    puzzleInput.forEachIndexed() { index, row ->
        val (left, right) = row.split(":")[1].split("|")

        val leftNumbers = "(\\d+)".toRegex().findAll(left).map { it.value }.toList()
        val rightNumbers = "(\\d+)".toRegex().findAll(right).map { it.value }.toList()

        val foundNumbers = leftNumbers.filter { it in rightNumbers }

//        println(foundNumbers.count())

        totalCount[index + 1] = foundNumbers.count()

    }
    println(totalCount)
    val winningsNumberCount = totalCount.toList()
    println(winningsNumberCount)

    winningsNumberCount.forEach() { (cardNumber, winningsNumberCount) ->
        repeat(winningsNumberCount) {
            println("$cardNumber $it $winningsNumberCount")
            totalCount[cardNumber + it + 1] = checkNotNull(totalCount[cardNumber + it + 1]) + checkNotNull(totalCount[cardNumber])

        }
    }

    println(totalCount.map { it.value }.sum())
}