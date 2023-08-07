package wordsvirtuoso

import java.io.File
import kotlin.system.exitProcess

class Words() {
    fun start(args: Array<String>) {
        when {
            args.size != 2 -> "Error: Wrong number of arguments."
            !File(args[0]).exists() -> "Error: The words file ${args[0]} doesn't exist."
            !File(args[1]).exists() -> "Error: The candidate words file ${args[1]} doesn't exist."
            else -> checkFiles(args[0], args[1])
        }.let(::println)
    }

    fun checkFiles(allWords: String, candidatesWords: String): String {
        val allWordsList = checkInvalidWords(allWords)
        val candidatesWordsList = checkInvalidWords(candidatesWords).toMutableList()

        candidatesWordsList.removeAll(allWordsList)
        return if (candidatesWordsList.size != 0)
            "Error: ${candidatesWordsList.size} candidate words are not included in the $allWords file."
        else "Words Virtuoso"
    }

    fun checkInvalidWords(fileName: String): List<String> {
        val listOfWords = File(fileName).readLines().map { it.lowercase() }
        var numberInvalidWords = 0
        listOfWords.forEach {
            if (!Regex("[a-z]{5}").matches(it) || it.toSet().size != 5) {
                numberInvalidWords++
            }
        }
        return if (numberInvalidWords != 0) {
            println("Error: $numberInvalidWords invalid words were found in the $fileName file.")
            exitProcess(0)
        } else listOfWords
    }

    fun startGame () {
        println("dghf")
    }
}

fun main(args: Array<String>) {
    Words().start(args)
}