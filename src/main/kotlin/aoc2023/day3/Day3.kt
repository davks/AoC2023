package aoc2023.day3

import java.io.File

val puzzleInput = loadFile("src/main/kotlin/aoc2023/day3/input.txt")
val correctNumbers = mutableListOf<Int>()
val testedChars = listOf<Char>('0', '1', '2', '3', '4', '5', '6', '7', '8', '9', '.')

fun main() {
    doFirst()
}

private fun doFirst() {
    var row = 0
    for (line in puzzleInput) {
        row++
        val regex = "[0-9]+".toRegex()
        val matches = regex.findAll(line) // find all numbers on row

        for (match in matches) {
            val correctNumber = checkBeforeAfter(line, match) // char before / after a number

            if (correctNumber != null) {
                correctNumbers.add(correctNumber.toInt())
                continue
            }

            when (row) {
                1 -> { // first row
                    val rowAfter = checkRowAfter(match, row, line)
                    if (rowAfter.any { it !in testedChars }) {
                        correctNumbers.add(match.value.toInt())
                    }
                }

                puzzleInput.size -> { // last row
                    val rowBefore = checkRowBefore(match, row, line)
                    if (rowBefore.any { it !in testedChars }) {
                        correctNumbers.add(match.value.toInt())
                    }
                }

                else -> {
                    val rowAfter = checkRowAfter(match, row, line)
                    val rowBefore = checkRowBefore(match, row, line)
                    if (rowBefore.any { it !in testedChars } || rowAfter.any { it !in testedChars }) {
                        correctNumbers.add(match.value.toInt())
                    }
                }
            }
        }
    }

    println(correctNumbers.sumOf { it })
}

fun checkRowAfter(match: MatchResult, row: Int, line: String): String {
    val begin = if (match.range.first == 0) 0 else match.range.first - 1
    val end = if (match.range.last == line.length - 1) match.range.last else match.range.last + 2
    val rowAfter = puzzleInput[row].substring(begin, end)

    return rowAfter
}

fun checkRowBefore(match: MatchResult, row: Int, line: String): String {
    val begin = if (match.range.first == 0) 0 else match.range.first - 1
    val end = if (match.range.last == line.length - 1) match.range.last else match.range.last + 2
    val rowBefore = puzzleInput[row - 2].substring(begin, end)

    return rowBefore
}

fun checkBeforeAfter(line: String, match: MatchResult): String? {
    var correctNumber: String? = null
        val oneAfter = if (line.length - 1 == match.range.last) match.range.last else match.range.last + 1 // check if last number
        val oneBefore = if (match.range.first == 0) 0 else match.range.first - 1 // check if first number

        if (line[oneAfter] !in testedChars || line[oneBefore] !in testedChars) {
            return match.value
        }

    return correctNumber
}

fun loadFile(fileName: String): List<String> = File(fileName).readLines()