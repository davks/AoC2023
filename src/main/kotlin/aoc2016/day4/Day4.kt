package aoc2016.day4

import java.io.File

data class Room(val name: String, val id: Int, val checksum: String)

fun main() {
    val day1 = File("src/main/kotlin/aoc2016/day4/input.txt").readLines()
    println(doFirst(day1))
}

fun doFirst(lines: List<String>): Int {
    var count = 0

    for (line in lines) {
        if (line.startsWith("#")) continue
        val room = getRoom(line)
        val letters = findChecksum(room.name)

        if (letters == room.checksum) {
            decryptRoom(room)
            count += room.id
        }
    }
    return count
}

fun decryptRoom(room: Room) {
    var decryptedName = ""
    for (char in room.name) {
        decryptedName += if (char == '-') " " else rotateChars(char, room.id % 26)
    }
    println("$decryptedName [${room.id}]")
}

fun rotateChars(char: Char, count: Int): Char {
    var newChar = char + count
    if (newChar > 'z')
        newChar = 'a' + (newChar - 'z' - 1)

    return newChar
}

fun rotateChars2(char: Char, count: Int) =
    if(char + count > 'z') 'a' + (char + count - 'z' - 1) else char + count

fun findChecksum(str: String): String {
    val letters = str.replace("-", "").groupingBy { it }.eachCount()

    //val a = letters.toList().sortedByDescending { it.second }.toMap()
//    return letters.toList().sortedWith(compareBy({it.second}, {it.first})).toMap()
    val result = letters.toList().sortedWith(compareByDescending<Pair<Char, Int>>{ it.second }.thenBy { it.first }).toMap()
    return result.keys.joinToString("").slice(0..4)
}

fun getRoom(str: String): Room {
    val regex = "(.+)-(\\d{3})\\[(\\w{5})]".toRegex()
    val matchResult = regex.find(str)

    if (matchResult != null) {
        return Room(
            matchResult.groupValues[1],
            matchResult.groupValues[2].toInt(),
            matchResult.groupValues[3]
        )
    } else {
        throw Exception()
    }
}
