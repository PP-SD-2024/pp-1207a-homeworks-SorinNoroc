package org.example
import kotlin.coroutines.*

fun main() {
    val eps = 1E-10 // suficient, dar ar putea fi si 10^(-15)
    tailrec fun findFixPoint(x: Double = 1.0): Double =
        if (Math.abs(x-Math.cos(x)) < eps) x else findFixPoint(Math.cos(x))
    runBlocking {
        launch {
            delay(1000L)
            fibonacci(100, 0, 0)
        }
    }
}

tailrec suspend fun fibonacci(n: Int, a: Long, b: Long): Long{return if (n == 0) a else fibonacci(n-1, b, a+b)
}