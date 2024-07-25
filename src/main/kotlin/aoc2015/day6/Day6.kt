package aoc2015.day6

import java.io.File

private data class Information(
    val turn: String,
    val startRow: Int,
    val startColumn: Int,
    val endRow: Int,
    val endColumn: Int
)

private data class Light(
    val brightness: Int
)

fun main() {
    val day6 = File("src/main/kotlin/aoc2015/day6/input.txt").readLines()
    println(doFirst(day6))
    println(doSecond(day6))

    val lightI2Da = Array(1000) { Array(1000) { 0 } }
    println(doAll(day6, lightI2Da, ::doFirstWhen) { it.flatten().sumOf { it } })

    val lightI2Db = Array(1000) { Array(1000) { 0 } }
    println(doAll(day6, lightI2Db, ::doSecondWhen) { it.flatten().sumOf { it } })

    val lightB2D = Array(1000) { Array(1000) { false } }
    println(doAll(day6, lightB2D, ::doThirdWhen) { it.flatten().count { it } })

    val lightL2D = Array(1000) { Array(1000) { Light(0) } }
    println(doAll(day6, lightL2D, ::doFourthtWhen) { it.flatten().sumOf { it.brightness } })
}

private fun doFirst(lines: List<String>): Int {
    val light2D = Array(1000) { Array(1000) { 0 } }

    for (line in lines) {
        val (turn, startRow, startColumn, endRow, endColumn) = getInfo(line)

        for (i in startRow..endRow) { // řádky
            for (j in startColumn..endColumn) { // sloupce
                light2D[i][j] = doFirstWhen(turn, light2D[i][j])
            }
        }
    }

    return getResult(light2D)
}

private fun doSecond(lines: List<String>): Int {
    val light2D = Array(1000) { Array(1000) { 0 } }

    for (line in lines) {
        val (turn, startRow, startColumn, endRow, endColumn) = getInfo(line)

        for (i in startRow..endRow) { // řádky
            for (j in startColumn..endColumn) { // sloupce
                light2D[i][j] = doSecondWhen(turn, light2D[i][j])
            }
        }
    }

    return getResult(light2D)
}

private fun <T> doAll(
    lines: List<String>,
    array2D: Array<Array<T>>,
    doIt: (String, T) -> T,
    result: (Array<Array<T>>) -> Int
): Int {
    for (line in lines) {
        val (turn, startRow, startColumn, endRow, endColumn) = getInfo(line)

        for (i in startRow..endRow) { // řádky
            for (j in startColumn..endColumn) { // sloupce
                array2D[i][j] = doIt(turn, array2D[i][j])
            }
        }
    }

    return result(array2D)
}

private fun doFirstWhen(turn: String, number: Int) =
    when (turn) {
        "turn on" -> 1
        "turn off" -> 0
        "toggle" -> if (number == 1) 0 else 1
        else -> -1
    }

private fun doSecondWhen(turn: String, number: Int) =
    when (turn) {
        "turn on" -> number + 1
        "turn off" -> if (number > 1) number - 1 else 0
        "toggle" -> number + 2
        else -> -1
    }

private fun doThirdWhen(turn: String, element: Boolean) =
    when (turn) {
        "turn on" -> true
        "turn off" -> false
        "toggle" -> !element
        else -> false
    }

private fun doFourthtWhen(turn: String, element: Light) =
    when (turn) {
        "turn on" -> Light(element.brightness + 1)
        "turn off" -> if (element.brightness > 1) Light(element.brightness - 1) else Light(0)
        "toggle" -> Light(element.brightness + 2)
        else -> Light(-1)
    }

private fun getInfo(line: String): Information {
    val regex = "(turn on|turn off|toggle) (\\d{1,3}),(\\d{1,3}) through (\\d{1,3}),(\\d{1,3})".toRegex()
    val result = regex.find(line)

    val (turn, startRow, startColumn, endRow, endColumn) = result!!.destructured
    return Information(turn, startRow.toInt(), startColumn.toInt(), endRow.toInt(), endColumn.toInt())
}

private fun getResult(lights2D: Array<Array<Int>>) = lights2D
    .flatten()
    .sumOf { it }
