
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
            <body>${s.joinToString("\n")} 
            </body>
        </html>
        """)
    }
}

class Content(private var author: String, private var text: String, private var name: String, private var publisher: String) {
    // For author
    fun getAuthor() = author
    fun setAuthor(au: String) {
        this.author = au
    }
    // For text
    fun getText() = text
    fun setText(te: String) {
        this.text = te
    }
    // For name
    fun getName() = name
    fun setName(na: String) {
        this.name = na
    }
    // For publisher
    fun getPublisher() = publisher
    fun setPublisher(pub: String) {
        this.publisher = pub
    }
}
open class Book(private var data: Content, private val price: Double) {
    override fun toString(): String {
        val book = "author: ${data.getAuthor()}\nname: ${data.getName()}\npublisher: ${data.getPublisher()}\ncontent: ${data.getText()}"
        return book
    }
    // Get name, author, publisher, content
    fun getPrice() = price
    fun getName() = data.getName()
    fun getAuthor() = data.getAuthor()
    fun getPublisher() = data.getPublisher()
    fun getContent() = data.getText()
    // Boolean fun for verifications (author, title, publisher)
    fun hasAuthor(au: String) = data.getAuthor() == au
    fun hasTitle(na: String) = data.getName() == na
    fun isPublisherBy(pub: String) = data.getPublisher() == pub
}

class TextBook(data: Content, price: Double): Book(data, price) {
    private val isBlank: Boolean = true

    fun isBlank(): Boolean {
        return isBlank
    }
}

class ComicBook(
    private val data: Content,
    private val movieInspired: String,
    private val hero: String,
    private val villain: String,
    private val price: Double
): Book(data, price) {
    fun getHero(): String {
        return hero
    }

    fun getVillain(): String {
        return villain
    }
}
class Library(private var books: Set<Book>) {
    fun getBooks(): Set<Book> = books
    fun addBook(b: Book) {
        books.plus(b)
    }
    // Find functions(author, name, publisher)
    fun findAllByAuthor(au: String): Set<Book> {
        var found: Set<Book> = HashSet()
        books.forEach { book ->
            if(book.getAuthor() == au) {
                found.plus(book)
            }
        }
        return found
    }
    fun findAllByName(na: String): Set<Book> {
        var found: Set<Book> = HashSet()
        books.forEach { book ->
            if(book.getName() == na) {
                found.plus(book)
            }
        }
        return found
    }
    fun findAllByPublisher(pub: String): Set<Book> {
        var found: Set<Book> = HashSet()
        books.forEach { book ->
            if(book.getPublisher() == pub) {
                found.plus(book)
            }
        }
        return found
    }

    fun printBooks(type: String) {
        when (type) {
            "json" -> LibraryPrinter().printBooks(books, JSONPrinter())
            "html" -> LibraryPrinter().printBooks(books, HTMLPrinter())
            else -> LibraryPrinter().printBooks(books, RawPrinter())
        }
    }
}
class LibraryPrinter {

    fun printBooks(books: Set<Book>, printer: Printer) {
        printer.print(books)
    }

    fun printBooksRaw(books: Set<Book>) {
        RawPrinter().print(books)
    }
    fun printHTML(books: Set<Book>) {
        HTMLPrinter().print(books)

    }
    fun printJSON(books: Set<Book>) {
        JSONPrinter().print(books)
    }
}

fun main() {
    val content1 = Content("Eminem", "Mergea prin padure", "Scufita Rosie", "Eminem")
    val content2 = Content("Eminem", "Better than Java", "Kotlin", "Eminem")
    content2.setName("Story")
    content2.setText("A fost odata ca niciodata")
    content2.setAuthor("Andersen")
    content2.setPublisher("Ziar de Iasi")
    val book1 = Book(content1, 28.50)
    val book2 = ComicBook(content2, "Thor", "Thor", "Loki", 25.00)
    val lib = Library(setOf(book1, book2))
    println("Fisierul Kotlin: ")
    lib.printBooks("json")
}