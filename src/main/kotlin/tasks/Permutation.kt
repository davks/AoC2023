package tasks

fun main() {
    val items = listOf('a', 'b', 'c', 'd')
    val permutations = permute(items)
    permutations.forEach { println(it) }
}

fun <T> permute(list: List<T>): List<List<T>> {
    if (list.isEmpty()) return listOf(emptyList())

    val result = mutableListOf<List<T>>()

    for (i in list.indices) {
        val current = list[i]
        val remaining = list.take(i) + list.drop(i + 1)
        for (permutation in permute(remaining)) {
            result.add(listOf(current) + permutation)
        }
    }

    return result
}
