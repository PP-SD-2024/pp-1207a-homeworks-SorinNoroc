package out

// Aplicatie 1

class Birth(val year: Int, val Month: Int, val Day: Int){
    override fun toString() : String{
        return "($Day.$Month.$year)"
    }
}

class Contact(val Name: String, var Phone: String, val BirthDate: Birth){
    fun Print() {
        println("Name: $Name, Mobile: $Phone, Date: $BirthDate")
    }

    // Punct 2
    fun updatePhone(newPhone: String) {
        if (Regex("0\\d{9}").matches(newPhone)) {
            Phone = newPhone
        } else {
            println("Wrong phone format! 10 Digits starting with 0 required.")
        }
    }
}

// Punct 1
fun MutableList<Contact>.searchContact(data: String): Contact? {
    for (item in this) {
        if (item.Name == data || item.Phone == data) {
            return item
        }
    }
    return null
}

fun main() {
    val agenda = mutableListOf<Contact>()

    agenda.add(Contact("Mihai", "0744321987", Birth(1900, 11, 25)))
    agenda += Contact("George", "0761332100", Birth(2002, 3, 14))
    agenda += Contact("Liviu" , "0231450211", Birth(1999, 7, 30))
    agenda += Contact("Popescu", "0211342787", Birth(1955, 5, 12))
    for (persoana in agenda){
        persoana.Print()
    }

    println("Agenda dupa eliminare contact [George]:")
    agenda.removeAt(1)
    for (persoana in agenda){
        persoana.Print()
    }

    agenda.remove(Contact("Liviu" , "0231450211", Birth(1999, 7, 30)))
    println("Agenda dupa eliminare contact [Liviu]:")

    // Testing
    agenda[0].updatePhone("0777777777")

    agenda.searchContact("0777777777").run {
        if (this != null) {
            this.Print()
        } else {
            println("Nu avem asa contact!")
        }
    }

    agenda.searchContact("354544").run {
        if (this != null) {
            this.Print()
        } else {
            println("Nu avem asa contact!")
        }
    }

    agenda.searchContact("Popescu").run {
        if (this != null) {
            this.Print()
        } else {
            println("Nu avem asa contact!")
        }
    }
}
