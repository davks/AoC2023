package aoc2023.day2

import java.io.File

data class Cube(val diceNumber: Int, val color: String)

fun main() {
    val puzzleInput = loadFile("src/main/kotlin/aoc2023/day2/input.txt")
    doFirst(puzzleInput)
    doSecond(puzzleInput)
}

fun doFirst(puzzleInput: List<String>) {
    val possibleGames = mutableListOf<Int>()

    for (line in puzzleInput) {
        val gameNumber = getGameNumber(line)
        val lineMatches = getMatchesLine(line)

        if (isGamePossible(lineMatches, line)) {
            possibleGames.add(gameNumber!!)
        }
    }

    println(possibleGames.sumOf { it })
}

fun doSecond(puzzleInput: List<String>) {
    val possibleGames = mutableListOf<Int>()

    for (line in puzzleInput) {
        val lineMatches = getMatchesLine(line)

        var cubeGreen: Cube? = null
        var cubeBlue: Cube? = null
        var cubeRed: Cube? = null

        for (matchResult in lineMatches) {
            val cubeTemp = matchResult.value.split(" ")
            val cube = Cube(cubeTemp[0].toInt(), cubeTemp[1])

            when (cube.color) {
                "green" -> cubeGreen = getCube(cube, cubeGreen)
                "blue" -> cubeBlue = getCube(cube, cubeBlue)
                "red" -> cubeRed = getCube(cube, cubeRed)
            }
        }
        val lineSum = cubeGreen?.diceNumber!! * cubeBlue?.diceNumber!! * cubeRed?.diceNumber!!
        possibleGames.add(lineSum)
    }

    println(possibleGames.sumOf { it })
}

fun getCube(cube: Cube, coloredCube: Cube?): Cube? {
    if (coloredCube == null || cube.diceNumber > coloredCube.diceNumber) return cube
    return coloredCube
}

fun isGamePossible(lineMatches: Sequence<MatchResult>, line: String): Boolean {
    val possibleCubes = listOf<Cube>(Cube(12, "red"), Cube(13, "green"), Cube(14, "blue"))

    lineMatches.forEach {
        val cubeTemp = it.value.split(" ")
        val cube = Cube(cubeTemp[0].toInt(), cubeTemp[1])

        possibleCubes.forEach { possibleCube ->
            if (cube.diceNumber > possibleCube.diceNumber && cube.color == possibleCube.color) {
                return false
            }
        }
    }

    return true
}

fun getGameNumber(line: String): Int? = "Game (\\d+)".toRegex().find(line)?.groupValues?.get(1)?.toInt()

fun getMatchesLine(line: String): Sequence<MatchResult> =
    "(([0-9]+) (blue|red|green)(?=[,;])?)+?".toRegex().findAll(line)

fun loadFile(fileName: String): List<String> = File(fileName).readLines()
