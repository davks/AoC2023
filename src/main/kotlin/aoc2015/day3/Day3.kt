package aoc2015.day3

import java.io.File

fun main() {
    val text = File("src/main/kotlin/aoc2015/day3/input.txt").readText() //2081, 2341
    println(doFirst(text))
    println(doFirst2(text.split("").toList()))
    println(doFirst3(text.split("").toList()))

    val doFirst4 = DoFirst4(text.split("").toList())
    println(doFirst4.findNumberOfHouses())

    println(doFirst5(text))
    println(doFirst6(text))

    println(doSecond(text))
}

private fun doFirst(text: String): Int {
    val houses = mutableSetOf<String>()
    val housesArray = text.split("")

    var x: Int = 0
    var y: Int = 0

    for (house in housesArray) {
        when (house) {
            "^" -> y++
            ">" -> x++
            "v" -> y--
            "<" -> x--
        }
        houses.add("${x}.${y}")
    }

    return houses.size
}

private tailrec fun doFirst2(housesList: List<String>, x: Int = 0, y: Int = 0, houses: MutableSet<String> = mutableSetOf()): Int {
    if (housesList.isEmpty()) return houses.size

    var x = x
    var y = y

    val place = when (housesList[0]) {
        "^" -> y++
        ">" -> x++
        "v" -> y--
        "<" -> x--
        else -> x
    }

    houses.add("${x}.${y}")

    return doFirst2(housesList.drop(1), x, y, houses)
}

private tailrec fun doFirst3(housesList: List<String>, xy: String = "0.0", houses: MutableSet<String> = mutableSetOf()): Int {
    if (housesList.isEmpty()) return houses.size

    var (x, y) = xy.split(".").map { it.toInt() }

    val place = when (housesList[0]) {
        "^" -> y++
        ">" -> x++
        "v" -> y--
        "<" -> x--
        else -> x
    }

    houses.add("${x}.${y}")

    return doFirst3(housesList.drop(1), "${x}.${y}", houses)
}

class DoFirst4(private val housesArray: List<String>) {
    private data class XY(var x: Int, var y: Int)

    private val xy = XY(0, 0)

    fun findNumberOfHouses(): Int {
        val houses = mutableSetOf<String>()

        for (house in housesArray) {
            val xy = getDimension(house)
            houses.add("${xy.x}.${xy.y}")
        }

        return houses.size
    }

    private fun getDimension(house: String): XY {
        when (house) {
            "^" -> xy.y++
            ">" -> xy.x++
            "v" -> xy.y--
            "<" -> xy.x--
        }

        return xy
    }
}

private fun doFirst5(text: String): Int = text.asSequence()
    .runningFold(Pair(0,0)) { xy, house ->
        when (house) {
            '^' -> xy.first to xy.second + 1
            '>' -> xy.first + 1 to xy.second
            'v' -> xy.first to xy.second - 1
            else -> xy.first - 1 to xy.second
        }
    }
    .distinct()
    .count()

private fun doFirst6(text: String): Int = text.asSequence()
    .runningFold(0 to 0) { (x, y), house ->
        when (house) {
            '^' -> x to y + 1
            '>' -> x + 1 to y
            'v' -> x to y - 1
            else -> x - 1 to y
        }
    }
    .distinct()
    .count()

private fun doSecond(text: String): Int {
    val santaOne = text.filterIndexed { index, _ -> index % 2 == 1 }
    val santaTwo = text.filterIndexed { index, _ -> index % 2 == 0 }

    return (getHouses(santaOne) + getHouses(santaTwo)).size
}

private fun getHouses(text: String) = text.asSequence()
    .runningFold(0 to 0) { (x, y), house ->
        when (house) {
            '^' -> x to y + 1
            '>' -> x + 1 to y
            'v' -> x to y - 1
            else -> x - 1 to y
        }
    }
    .toSet()

private fun getHouses(housesArray: List<String>): Set<String> {
    val houses = mutableSetOf<String>()

    var x: Int = 0
    var y: Int = 0

    for (house in housesArray) {
        when (house) {
            "^" -> y++
            ">" -> x++
            "v" -> y--
            "<" -> x--
        }
        houses.add("${x}.${y}")
    }

    return houses
}
