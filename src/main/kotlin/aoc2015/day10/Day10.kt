package aoc2015.day10

fun main() {
    var text = "1321131112"

    for (i in 0 until 50) {
        text = findNumbers2(text)
    }

    println(text.length)
}

fun findNumbers2(text: String) : String {
    // 0+|1+|2+|3+|4+|5+|6+|7+|8+|9+
    // 00*|11*|22*|33*|44*|55*|66*|77*|88*|99*
    // (\d)\1+|(\d) - backreference
    val regex = "(\\d)\\1+|(\\d)".toRegex()
    return regex.findAll(text).map { it.value }.toList().joinToString("") { "${it.length}${it.first()}" }
}
