
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


open class Book(private var data: Content) {
    override fun toString(): String {
        val book = "author: ${data.getAuthor()}\nname: ${data.getName()}\npublisher: ${data.getPublisher()}\ncontent: ${data.getText()}"
        return book
    }
    // Get name, author, publisher, content
    fun getName() = data.getName()
    fun getAuthor() = data.getAuthor()
    fun getPublisher() = data.getPublisher()
    fun getContent() = data.getText()
    // Boolean fun for verifications (author, title, publisher)
    fun hasAuthor(au: String) = data.getAuthor() == au
    fun hasTitle(na: String) = data.getName() == na
    fun isPublisherBy(pub: String) = data.getPublisher() == pub
}


class TextBook(private val price: Int, val data: Content): Book(data) {
    private val isBlank: Boolean = true

    fun isBlank() = isBlank
    
    fun getPrice() = price
}


class ComicBook(
    private val data: Content,
    private val movieInspired: String,
    private val hero: String,
    private val villain: String,
    private val price: Int
): Book(data) {
    fun getHero() = hero

    fun getVillain() = villain
}


class Library(private var books: Set<Book>) {
    fun getBooks(): Set<Book> = books
    fun addBook(b: Book) {
        books.plus(b)
    }
    
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
    var content1: Content = Content("Gigel", "Acesta este cel mai minunat text", "Text", "My books")
    var content2: Content = Content("Matei", "Acesta este Alt text genial", "AltText", "My books")
    var book1: Book = Book(content1)
    var book2: Book = Book(content2)
    var lib: Library = Library(setOf(book1, book2))
    lib.printBooks("json")
    lib.printBooks("html")

}