package tasks

import javax.naming.Name

fun main() {
    val person1 = person("David", "Ka")
    person1.setFirstName("Kamil")
    println(person1.getFullName())

    val (getFirstName, getFullName, setFirstName) = person("Radim", "Ka")
    setFirstName("Martin")
    println(getFullName())
}

data class Person(val getFirstName: () -> String, val getFullName: () -> String, val setFirstName: (String) -> Unit)

fun person(name: String, surname: String): Person {
    var firstName = name
    var secondName = surname

    fun getFirstName() = firstName
    fun getFullName() = "$firstName $secondName"
    fun setFirstName(name: String) {
        firstName = name
    }

    return Person(
        ::getFirstName,
        ::getFullName,
        ::setFirstName
    )
}