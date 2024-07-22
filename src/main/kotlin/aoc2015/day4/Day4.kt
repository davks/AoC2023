package aoc2015.day4

import aoc2015.*
import java.math.BigInteger
import java.security.MessageDigest

fun main() {
    println(doFirst("iwrupvqb", 5)) // iwrupvqb 346386
//    println(doFirst("iwrupvqb", 6)) // iwrupvqb 9958218

    println(doFirst2("iwrupvqb", 5))
    println(doFirst3("iwrupvqb", 5))
    println(doFirst4("iwrupvqb", 5))
    println(doFirst5("iwrupvqb", 5))
}

private fun doFirst(key: String, numberOfZerosInHash: Int): String {
    for (number in 0..Int.MAX_VALUE) {
        val numericKey = number.padStart(6)
        val hash = "${key}${numericKey}".md5()

        if (hash.startsWith(0.padStart(numberOfZerosInHash))) return "$hash : $numericKey"
    }

    return "Didn't find"
}

private tailrec fun doFirst2(key: String, numberOfZerosInHash: Int, number: Int = 0, numericKey: String = ""): String {
    if (number == Int.MAX_VALUE) return "Doesn't find"

    val numericKey = number.padStart(6)
    val hash = "${key}${numericKey}".md5()

    if (hash.startsWith(0.padStart(numberOfZerosInHash))) return "$hash : $numericKey"

    return doFirst2(key, numberOfZerosInHash, number + 1, numericKey)
}

private fun doFirst3(key: String, numberOfZerosInHash: Int): String = (0..Int.MAX_VALUE)
    .asSequence()
    .takeIncluding { number ->
        val numericKey = number.padStart(6)
        val hash = "${key}${numericKey}".md5()

        hash.startsWith(0.padStart(numberOfZerosInHash))
    }
    .last()
    .toString()

private fun doFirst4(key: String, numberOfZerosInHash: Int): String = (0..Int.MAX_VALUE)
    .first { number ->
        val numericKey = number.padStart(6)
        val hash = "${key}${numericKey}".md5()
        hash.startsWith(0.padStart(numberOfZerosInHash))
    }
    .toString()

private fun doFirst5(key: String, numberOfZerosInHash: Int): String = (0..Int.MAX_VALUE)
    .asSequence()
    .filter { number ->
        val numericKey = number.padStart(6)
        val hash = "${key}${numericKey}".md5()
        hash.startsWith(0.padStart(numberOfZerosInHash))
    }
    .first()
    .toString()