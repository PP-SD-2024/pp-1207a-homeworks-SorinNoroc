class Cinema {
    private val movieList: MutableList<Movie> = mutableListOf()

    fun addMovie(movieName: String, price: Double, count: Int = 10) {
        val movie = Movie(movieName, price, count)
        movieList.add(movie)
    }

    fun selectMovie(movieName: String): Movie? {
        for (movie in movieList) {
            if (movie.getName() == movieName) {
                return movie
            }
        }
        println("No such movie $movieName!")
        return null
    }
}

class Movie(
    private val name: String,
    private val ticketPrice: Double,
    private var ticketCount: Int
) {
    fun getName() = name
    fun buyTicket(pay: PaymentMethod): Ticket? {
        return if (pay.pay(ticketPrice)) {
            ticketCount--
            println("Ticket for movie $name bought for $ticketPrice!")
            Ticket(ticketPrice)
        } else {
            println("Not enough funds! Get some money!")
            return null
        }
    }
}

data class Ticket(private val price: Double) {
    fun getPrice() = price
}