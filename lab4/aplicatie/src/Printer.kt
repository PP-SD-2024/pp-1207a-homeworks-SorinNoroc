interface Printer {
    fun print(s: Set<Book>)
}

class RawPrinter: Printer {
    override fun print(s: Set<Book>) {
        s.forEach { book ->
            println(book)
        }
    }
}

class JSONPrinter: Printer {
    override fun print(s: Set<Book>) {
        var count = 1
        println("{")
        for (book in s) {
            println("\tBook $count: $book")
            count++
        }
        println("}")
    }
}

class HTMLPrinter: Printer {
    override fun print(s: Set<Book>) {
        println("""
        <html>
            <head>
                <title>
                    Books
                </title>
            </head>
            <body>
                ${s.joinToString("\n")} 
            </body>
        </html>
        """)
    }
}