package aoc2015.day11

// hxbxwxba
// hxbxxyzz
// hxcaabcc
fun main() {

    var input = "hxbxwxba"
    val newPassword = getNewPassword(input)
    println(newPassword)
    println(getNewPassword(newPassword))
}

fun getNewPassword(input: String): String {
    var input = input

    while (true) {
        input = increaseByOne(input)
        if (!isSomeLetters(input) and isSequence(input) and isPairs(input)) {
            return input
        }
    }
}

fun increaseByOne(input: String): String {
    val chars = input.toCharArray()

    for (i in chars.size - 1 downTo 0) {
        if (chars[i] == 'z') {
            chars[i] = 'a'
        } else {
            chars[i] = chars[i] + 1
            break;
        }
    }

    return String(chars)
}

fun isPairs(input: String): Boolean {
    var count = 0

    var i: Int = 0
    while (i < input.length - 1) {
        if (input[i] == input[i + 1]) {
            count++
            i += 2
        } else {
            i++
        }
    }

    return count >= 2
}

fun isPairs2(input: String): Boolean {
    var count = 0
    var lastChar = '1'

    for (i in 0..input.length - 2) {
        if (input[i] == lastChar) continue

        if (input[i] == input[i + 1]) {
            lastChar = input[i]
            count++
        }
    }

    return count >= 2
}

fun isSequence(input: String): Boolean {
    for (i in 0 .. input.length - 3) {
        val second = input[i + 1] == input[i] + 1
        val third = input[i + 2] == input[i] + 2

        if (second and third) return true
    }

    return false
}

fun isSomeLetters(input: String): Boolean =
    ('i' in input) and ('l' in input) and ('o' in input)


fun increaseByOne2(input: String): String {
    val chars = input.toCharArray()

    var i = chars.size - 1;
    while (i >= 0 && input[i] == 'z') {
        chars[i--] = 'a'
    }
    chars[i]++;

    return String(chars)
}
