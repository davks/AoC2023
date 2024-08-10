package aoc2015.day7

import java.io.File

// Last version 7A and 7B

/*
* 1. Nezkracovat seznamy při jeho opakovaném procházení, ale vytvářet nové,
*    kopírovat je a až pak je znovu procházet.
* 2. Lépe jsem pochopil sealed class a hodí se tam, kde potřebuju pracovat s pevně danými datovými typy,
*    které potřebuju testovat pomocí `when` - vlastně mi to trochu připomíná enum.
*    Řekl bych, že hlavní výhoda je v tom, že to pomáhá programátorovi vyhýbat se chybám.
* 3. Připravit si data dopředu do lepšího formátu a až pak s nimi pracovat.
* 4. Pokud používám dědičnost a potřebuje pracovat s objekty na které se odkazuji za pomocí nadřazené třídy,
*    musím testovat typ, abych s objektem mohl regulérně pracovat.
* 5. Rozšiřuje 4. o to, že se můžu zbavit testování typu díky tomu, že si vytvořím buďto značkovací interface
*    s metodou, kterou třídy, které se k interfejsu přihlásí, implementují,
*    nebo vytvořím abstraktní metodu v abstraktní třídě, které dceřiné třídy implementují.
*    Tady jsem použil sealed class, kvůli těm `when`, ale v poslední verzi by stačila jen abstraktní třída,
*    nebo interface.
*    Vzhledem k tomu, že abstraktní třída má i abstraktní atribut, tak je asi vhodnější abstraktní třída
*    před interfejsem.
* 6. Rekurze ve smyslu dej mi hodnotu a pokud mi ji nemůžeš hned dát, protože musíš ještě něco zjistit
*    (závisíš na něčem jiném), tak mě to nezajímá, prostě si to zjisti a mě vrať až výsledek.
*    Tady jsem se o to jen trošku otřel a cítím, že k plnému pochopení ještě potřebuju pár příkladů.
*/

/*
    map of signals:
    bo=BinarySignal(valueA=bn, operator=RSHIFT, valueB=2, name=bo)
    ly=BinarySignal(valueA=lf, operator=RSHIFT, valueB=1, name=ly)
    fq=BinarySignal(valueA=fo, operator=RSHIFT, valueB=3, name=fq)
    cq=BinarySignal(valueA=cj, operator=OR, valueB=cp, name=cq)
    ga=BinarySignal(valueA=fo, operator=OR, valueB=fz, name=ga)
    u=BinarySignal(valueA=t, operator=OR, valueB=s, name=u)
    a=UnarySignal(operator=COPY, value=lx, name=a)
    ay=UnarySignal(operator=NOT, value=ax, name=ay)
    ...
*/

fun main() {
    val day7 = File("src/main/kotlin/aoc2015/day7/input.txt").readLines()
    println(doFirst(day7))
    println(doFirst2(day7))
    println(doSecond(day7, doFirst2(day7)))
}

private enum class UnaryOperator {
    NOT, COPY
}

private enum class BinaryOperator {
    AND, OR, RSHIFT, LSHIFT
}

private fun doFirst(lines: List<String>): Int {
    val signals = lines.map { getOneOfTheSignal(it) }.associate {
        when (it) {
            is BinarySignal -> it.name to it
            is UnarySignal -> it.name to it
        }
    }
    return calc("a", signals, mutableMapOf())
}

private fun doFirst2(lines: List<String>): Int {
    val signals: Map<String, Signal> = lines.map(::getOneOfTheSignal).associateBy { it.name }
    return calc("a", signals, mutableMapOf())
}

private fun doSecond(lines: List<String>, signalA: Int): Int {
    val signals: MutableMap<String, Signal> = lines.map(::getOneOfTheSignal).associateBy { it.name }.toMutableMap()
    signals["b"] = UnarySignal(UnaryOperator.COPY, signalA.toString(), "b")
    return calc("a", signals, mutableMapOf())
}

/**
 * foundSignals - cache with values. key is s name of the signal and value is calculate number.
 */
private fun calc(signalName: String, signals: Map<String, Signal>, foundSignals: MutableMap<String, Int>): Int {
    if (signalName.toIntOrNull() != null) return signalName.toInt()
    if (foundSignals.containsKey(signalName)) return foundSignals[signalName]!! // return value from cache

    val result = signals[signalName]!!.calculate(signals, foundSignals) // call calculate - call recursion is inside
    foundSignals[signalName] = result // calculate value insert to cache

    return result
}

private sealed class Signal {
    abstract val name: String // name of the signal
    abstract fun calculate(
        signals: Map<String, Signal>,
        foundSignals: MutableMap<String, Int>
    ): Int // calculate the signal
}

private data class BinarySignal(
    val valueA: String,
    val operator: BinaryOperator,
    val valueB: String,
    override val name: String
) : Signal() {
    override fun calculate(signals: Map<String, Signal>, foundSignals: MutableMap<String, Int>): Int {
        val valueA = calc(this.valueA, signals, foundSignals)
        val valueB = calc(this.valueB, signals, foundSignals)

        return when (this.operator) {
            BinaryOperator.AND -> valueA and valueB
            BinaryOperator.OR -> valueA or valueB
            BinaryOperator.LSHIFT -> valueA shl valueB
            BinaryOperator.RSHIFT -> valueA shr valueB
        }
    }
}

// Dej mi svoji hodnotu a pokud máš nějaký závislosti, tak si je zjisti
private data class UnarySignal(
    val operator: UnaryOperator,
    val value: String,
    override val name: String
) : Signal() {
    override fun calculate(signals: Map<String, Signal>, foundSignals: MutableMap<String, Int>): Int {
        val value = calc(this.value, signals, foundSignals)

        return when (operator) {
            UnaryOperator.COPY -> value
            UnaryOperator.NOT -> 65536 + value.inv()
        }
    }
}

private fun getOneOfTheSignal(line: String): Signal {
    val regex1 = "^(\\w{1,3}) (AND|OR|RSHIFT|LSHIFT) (\\w{1,3}) -> (\\w{1,3})".toRegex()
    val regex2 = "^(NOT )?(\\w{1,5}) -> (\\w{1,3})".toRegex()

    fun getBinarySignal(matchResult: MatchResult): BinarySignal? {
        val (a, operator, b, result) = matchResult.destructured
        val eOperator = when (operator) {
            "AND" -> BinaryOperator.AND
            "OR" -> BinaryOperator.OR
            "LSHIFT" -> BinaryOperator.LSHIFT
            "RSHIFT" -> BinaryOperator.RSHIFT
            else -> throw IllegalArgumentException("Unknown operator")
        }
        return BinarySignal(a, eOperator, b, result)
    }

    fun getUnarySignal(matchResult: MatchResult): UnarySignal? {
        val (operator, a, result) = matchResult.destructured
        val eOperator = when (operator.trim()) {
            "NOT" -> UnaryOperator.NOT
            else -> UnaryOperator.COPY
        }
        return UnarySignal(eOperator, a, result)
    }

    var signal: Signal? = getSignal(regex1, line, ::getBinarySignal)
    signal = signal ?: getSignal(regex2, line, ::getUnarySignal)

    return signal!!
}

private fun <T : Signal> getSignal(regex: Regex, line: String, get: (MatchResult) -> T?): T? {
    val matchResult = regex.find(line)

    return if (matchResult != null) {
        get(matchResult)
    } else {
        null
    }
}
