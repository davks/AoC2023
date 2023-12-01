package day1


import java.io.File

class Day1(val fileName: String) {

    private var puzzleInput: List<String> = mutableListOf()

    fun doFirst() {
        loadFile()
        val sum = getSum()
        println(sum)
    }

    fun doSecond() {
        loadFile()
        changeInput()
        val sum = getSum()
        println(sum)
    }

    private fun loadFile() {
        puzzleInput = File(fileName).readLines()
    }

    private fun getSum(): Int {
        var sum = 0

        for (line in puzzleInput) {
            val match = findMatch(line)
            val number = getNumber(match!!)
            sum += number
        }

        return sum
    }

    private fun findMatch(line: String): String? {
        val regex = "([1-9].*[1-9]|[1-9])".toRegex()
        val matches = regex.find(line)
        return matches?.value
    }

    private fun getNumber(line: String): Int {
        val first = line[0]
        val second = line[line.length - 1]

        return (first.toString() + second.toString()).toInt()
    }

    private fun changeInput() {
        var newInput = mutableListOf<String>()
        for (line in puzzleInput) {
            newInput.add(replaceNumber(line))
        }

        puzzleInput = newInput
    }

//    private fun replaceNumber(line: String): String {
//        return line
//            .replace("one", "o1ne", true)
//            .replace("two", "t2wo", true)
//            .replace("three", "t3hree", true)
//            .replace("four", "f4our", true)
//            .replace("five", "f5ive", true)
//            .replace("six", "s6ix", true)
//            .replace("seven", "s7even", true)
//            .replace("eight", "e8ight", true)
//            .replace("nine", "n9ine", true)
//    }

    private fun replaceNumber(line: String): String {
        val numbers = listOf("0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "one", "two", "three", "four", "five", "six", "seven", "eight", "nine")
        val first = line.findAnyOf(numbers)?.second?.changeToDigit()
        val last = line.findLastAnyOf(numbers)?.second?.changeToDigit()
        return "$first$last"
    }

    private fun String.changeToDigit(): String {
        return when (this) {
            "one" -> "1"
            "two" -> "2"
            "three" -> "3"
            "four" -> "4"
            "five" -> "5"
            "six" -> "6"
            "seven" -> "7"
            "eight" -> "8"
            "nine" -> "9"
            else -> this
        }
    }
}