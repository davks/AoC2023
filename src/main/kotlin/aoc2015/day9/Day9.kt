package aoc2015.day9

import tasks.permute
import java.io.File

/*
    routes:
    [London, Dublin, Belfast]
    [London, Belfast, Dublin]
    [Dublin, London, Belfast]
    [Dublin, Belfast, London]
    [Belfast, London, Dublin]
    [Belfast, Dublin, London]

    distances:
    London={Dublin=464, Belfast=518}
    Dublin={London=464, Belfast=141}
    Belfast={London=518, Dublin=141}
 */

fun main() {
    val day9 = File("src/main/kotlin/aoc2015/day9/input-test.txt").readLines()
    println(doFirst4(day9))
    println(doSecond(day9))
}

private fun doFirst(list: List<String>): Int {
    val distances = parseDistances(list)
    val routes = permute(getRoutes(list))

    fun calculateRouteDistance(route: List<String>): Int {
        var totalDistance = 0
        for (i in 0 until route.size - 1) {
            val fromCity = route[i]
            val toCity = route[i + 1]

            totalDistance += distances[fromCity]?.get(toCity) ?: 0
        }
        return totalDistance
    }

    return routes.minOf(::calculateRouteDistance)
}

private fun doFirst2(list: List<String>): Int {
    val distances = parseDistances(list)
    val routes = permute(getRoutes(list))

    fun calculateRouteDistance(route: List<String>): Int {
        return route
            .zipWithNext { fromCity, toCity -> distances[fromCity]?.get(toCity) ?: 0 }
            .sum()
    }

    return routes.minOf(::calculateRouteDistance)
}

private fun doFirst3(list: List<String>): Int {
    val distances = parseDistances(list)
    val routes = permute(getRoutes(list))

    return routes.minOf {
        it
            .zipWithNext { fromCity, toCity -> distances[fromCity]?.get(toCity) ?: 0 }
            .onEach { println(it) }
            .sum()
    }
}

private fun doFirst4(list: List<String>): Int {
    val distances = parseDistances(list)
    val routes = permute(getRoutes(list))

    return routes.minOf {
        it
            .zipWithNext()
            .map { pair -> distances[pair.first]?.get(pair.second) ?: 0 }
            .sum()
    }
}

private fun doSecond(list: List<String>): Int {
    val distances = parseDistances(list)
    val routes = permute(getRoutes(list))

    return routes.maxOf {
        it
            .zipWithNext { fromCity, toCity -> distances[fromCity]?.get(toCity) ?: 0 }
            .sum()
    }
}

private fun parseDistances(list: List<String>): Map<String, Map<String, Int>> {
    val distances = mutableMapOf<String, MutableMap<String, Int>>()

    list.forEach { line ->
        val (city1, city2, distanceStr) = line.split(" to ", " = ")
        val distance = distanceStr.toInt()

        distances.computeIfAbsent(city1) { mutableMapOf() }[city2] = distance
        distances.computeIfAbsent(city2) { mutableMapOf() }[city1] = distance
    }

    return distances
}

private fun getRoutes(list: List<String>): List<String> = list
    .flatMap {
        val (city1, city2) = it.split(" to ", " = ")
        listOf(city1, city2)
    }
    .distinct()
    .toList()

private fun getRoutes2(list: List<String>): List<String> {
    val cities = mutableSetOf<String>()

    list.forEach { line ->
        val (city1, city2) = line.split(" to ", " = ")

        cities.add(city1)
        cities.add(city2)
    }

    return cities.toList()
}
