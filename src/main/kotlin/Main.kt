package wordsvirtuoso

import java.io.File
import kotlin.system.exitProcess

class Words(private val files: Array<String>) {
    private val allWordsList = mutableListOf<String>()
    private val listOfClue = mutableListOf<String>()
    private var incorrectLetters = ""
    private var secretWord = ""
    private var attemptNumber = 0
    private var startTime: Long = 0

    fun start() {
        when {
            files.size != 2 -> println("Error: Wrong number of arguments.")
            !File(files[0]).exists() -> println("Error: The words file ${files[0]} doesn't exist.")
            !File(files[1]).exists() -> println("Error: The candidate words file ${files[1]} doesn't exist.")
            else -> checkFiles(files[0], files[1])
        }
    }

    private fun checkFiles(allWordsFileName: String, candidatesWordsFileName: String) {
        allWordsList.addAll(checkInvalidWords(allWordsFileName))
        val candidatesWordsList = checkInvalidWords(candidatesWordsFileName).toMutableList()
        secretWord = candidatesWordsList.random().lowercase()
        candidatesWordsList.removeAll(allWordsList)
        if (candidatesWordsList.size != 0)
            println("Error: ${candidatesWordsList.size} candidate words are not included in the $allWordsFileName file.")
        else {
            println("Words Virtuoso")
            startGame()
        }
    }

    private fun checkInvalidWords(fileName: String): List<String> {
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

    private fun startGame () {
        startTime = System.currentTimeMillis()
        while (true) {
            println("\nInput a 5-letter word:")
            with(readln().lowercase()) {
                if (this == "exit") println("The game is over.").also { return }
                println(checkInputForErors(this) ?: checkInputIsSecretWord(this))
            }
        }
    }

    private fun checkInputForErors(input: String): String? {
        return when {
            input.length != 5 -> "The input isn't a 5-letter word."
            !Regex("[a-z]+").matches(input) -> "One or more letters of the input aren't valid."
            input.toSet().size != 5 -> "The input has duplicate letters."
            input !in allWordsList -> "The input word isn't included in my words list."
            else -> null
        }
    }

    private fun checkInputIsSecretWord (input: String): String {
        var clue = ""
        attemptNumber++
        for (index in 0..4) {
            clue += when {
                input[index] == secretWord[index] -> input[index].uppercase()
                input[index] in secretWord -> input[index]
                else -> "_".also { incorrectLetters += input[index].uppercase() }
            }
        }

        if (clue.lowercase() == secretWord) {
            if (attemptNumber == 1) {
                println("${clue.uppercase()}\nCorrect!")
                println("Amazing luck! The solution was found at once.")}
            else {
                println(listOfClue.joinToString("\n"))
                println("\n${clue.uppercase()}\nCorrect!")
                println("The solution was found after $attemptNumber tries " +
                        "in ${(System.currentTimeMillis() - startTime) / 100} seconds.")
            }
            exitProcess(1)
        }

        listOfClue.add(clue)
        return buildString {
            append(listOfClue.joinToString("\n"))
            append("\n${incorrectLetters.toSortedSet().joinToString("")}")
        }
    }
}

fun main(args: Array<String>) {
    Words(args).start()
}