
fun main() {
    val cinema = Cinema()
    cinema.addMovie("FNAF", 19.50)
    cinema.addMovie("Lord of the Rings", 25.0)
    cinema.addMovie("Tokyo Drift", 29.50)

    val bankAccount = BankAccount(30.23, "5574 5942 3851 4993", 299, "Raul")
    val payment = CardPayment(bankAccount)
    cinema.selectMovie("FNAF")?.buyTicket(payment)
    cinema.selectMovie("Tokyo Drift")?.buyTicket(payment)

    val pocket = CashPayment(22.59)
    cinema.selectMovie("Red Dead Redemption")?.buyTicket(pocket)
    cinema.selectMovie("Lord of the Rings")?.buyTicket(pocket)
    cinema.selectMovie("FNAF")?.buyTicket(pocket)
}