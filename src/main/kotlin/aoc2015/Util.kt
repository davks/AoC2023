package aoc2015

import java.math.BigInteger
import java.security.MessageDigest

fun String.md5(): String {
    val md = MessageDigest.getInstance("MD5")
    return BigInteger(1, md.digest(this.toByteArray())).toString(16).padStart(32, '0')
}

fun Int.padStart(length: Int, padChar: Char = '0'): String = this.toString().padStart(length, padChar)

fun <T> Sequence<T>.takeUntil(predicate: (T) -> Boolean): Sequence<T> = sequence {
    for (item in this@takeUntil) {
        if (predicate(item)) break
        yield(item)
    }
}
