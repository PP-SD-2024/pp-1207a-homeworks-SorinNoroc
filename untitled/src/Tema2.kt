

import java.io.File

fun main() {
    val text = readEbook("Fisier.txt")
    val textFaraNewline = eliminateMultipleNewLine(text)
    val textCurat = eliminateAll(textFaraNewline)
    print(textCurat)
}

fun readEbook(name: String): String {
    var text = File(name).readText()
    return text
}

fun eliminateMultipleNewLine(text: String): String {
    return Regex("\n{2,}").replace(text, "\n")
}

fun eliminateAll(text: String): String {
    // Eliminam spatii multiple, numarul de pagina, sau numele capitolului
    return Regex(" {2,}|\\s{2,}\\w+\\s{2,}").replace(text, " ")
}