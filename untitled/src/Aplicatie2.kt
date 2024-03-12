package out

import java.io.File

// Aplicatie 2

fun main(args : Array<String>){
    val Dictionar = hashMapOf<String, String>()
    Dictionar.put("rosu", "red")

    val Poveste = "Once upon a time there was an old woman who loved baking gingerbread. She would bake gingerbread cookies, cakes, houses and gingerbread people, all decorated with chocolate and peppermint, caramel candies and colored ingredients. Making children happy is all she wanted."
    var povesteTradusa = ""
    val words1 = Poveste.split(" ")

    // Initializare dictionar din fisier text
    val fisier = File("dictionar.epub")
    for (line in fisier.readLines()) {
        val entry = line.split(":")
        Dictionar[entry[0]] = entry[1]
    }
    println(Dictionar)

    println("Cuvintele din poveste [${words1.count()}]:")
    for (word in words1)
        print("$word ")

    val words2 = mutableListOf<String>()
    for (word in words1){
        words2.add(word.trim(',','.'))
    }

    println("\n")
    println("Povestea tradusa ar suna cam asa:")
    for (item in words2){
        povesteTradusa += Dictionar.getOrDefault(item, item) + " "
    }
    File("output.txt").writeText(povesteTradusa)
}