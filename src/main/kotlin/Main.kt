package wordsvirtuoso

fun main() {
    /**
     * if you remove the try catch expression and change "throw Exception" to "println",
     * the test will also be passed, but in real life there are other exception, such as EOF
     */
    println("Input a 5-letter string:")
    try {
        with(readln()) {
            when {
                this.length != 5 -> throw Exception("The input isn't a 5-letter string.")
                !"[a-zA-Z]+".toRegex().matches(this) -> throw Exception("The input has invalid characters.")
                this.toSet().size != 5 -> throw Exception("The input has duplicate letters.")
                else -> throw Exception("The input is a valid string.")
            }
        }
    } catch (e: Exception) {
        println(e.message)
    }
}