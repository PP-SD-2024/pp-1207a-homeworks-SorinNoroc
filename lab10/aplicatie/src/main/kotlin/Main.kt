import kotlinx.coroutines.*
import java.io.File
import java.lang.Thread.sleep

class Log private constructor() {
    companion object {
        val instance = Log()
        val fname = "Semafor.txt"
    }

    suspend fun iterate(list: IntProgression) {
        for (elem in list) {
            instance.Write(elem.toString())
        }
    }

    suspend fun Write(line : String) {
        // Wait for file to be writable
        while (Semafor.checkLocked()) { }
        Semafor.Enter()
        File(fname).appendText(line + "\n")
        Semafor.Exit()
    }

    fun Reset(){
        File(fname).delete()
    }
}

object Semafor {
    private var locked = false

    fun checkLocked(): Boolean {
        return locked
    }

    fun Enter() {
        locked = true
    }

    fun Exit() {
        locked = false
    }
}

fun main() = runBlocking {
    val evens = 0..10000 step 2 // 0, 2, 4...
    val odds = 1..10000 step 2 // 1, 3, 5...
    coroutineScope {
        for (num in evens) {
            Log.instance.iterate(evens)
        }
        for (num in odds) {
            Log.instance.iterate(odds)
        }
    }
    println(File(Log.fname).readText())
    Log.instance.Reset()

}
