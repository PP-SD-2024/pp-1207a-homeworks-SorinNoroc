interface PaymentMethod {
    fun pay(fee: Double): Boolean
    fun showFunds()
}

class CashPayment(private var availableAmount: Double): PaymentMethod {

    override fun pay(fee: Double): Boolean {
        if (availableAmount - fee > 0) {
            availableAmount -= fee
            return true
        } else {
            return false
        }
    }

    override fun showFunds() {
        println("Cash status: $availableAmount")
    }
}

class CardPayment(private val bankAccount: BankAccount): PaymentMethod {
    override fun pay(fee: Double): Boolean {
        return bankAccount.updateAmount(fee)
    }

    override fun showFunds() {
        println("Bank funds: ${bankAccount.getFunds()}")
    }
}

class BankAccount(
    private var availableAmount: Double,
    private val cardNumber: String,
    private val cvvCode: Int,
    private val userName: String
) {
    fun updateAmount(value: Double): Boolean {
        if (availableAmount - value >= 0) {
            availableAmount -= value
            return true
        } else {
            return false
        }
    }

    fun getFunds() = availableAmount
}