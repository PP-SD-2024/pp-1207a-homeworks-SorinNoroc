import java.util.*
import kotlin.math.pow


enum class Associativity {
    LEFT,
    RIGHT
}

enum class Operator(val symbol: Char, val associativity: Associativity, val precedence: Int) :
    Comparable<Operator> {
    ADDITION('+', Associativity.LEFT, 0),
    SUBTRACTION('-', Associativity.LEFT, 0),
    DIVISION('/', Associativity.LEFT, 5),
    MULTIPLICATION('*', Associativity.LEFT, 5),
    MODULUS('%', Associativity.LEFT, 5),
    POWER('^', Associativity.RIGHT, 10);

    fun comparePrecedence(operator: Operator): Int {
        return this.precedence - operator.precedence
    }
}

object ShuntingYard {
    private val OPS: MutableMap<Char, Operator> = HashMap()

    init {
        for (operator in Operator.values()) {
            OPS[operator.symbol] = operator
        }
    }

    fun shuntingYard(tokens: List<Char>): List<Char> {
        val output: MutableList<Char> = LinkedList()
        val stack = Stack<Char>()

        for (token in tokens) {
            if (OPS.containsKey(token)) {
                while (!stack.isEmpty() && OPS.containsKey(stack.peek())) {
                    val cOp = OPS[token] // Current operator
                    val lOp = OPS[stack.peek()] // Top operator from the stack
                    if ((cOp!!.associativity == Associativity.LEFT && cOp.comparePrecedence(lOp!!) <= 0) ||
                        (cOp.associativity == Associativity.RIGHT && cOp.comparePrecedence(lOp!!) < 0)
                    ) {
                        output.add(stack.pop())
                        continue
                    }
                    break
                }
                stack.push(token)
            } else if ('(' == token) {
                stack.push(token)
            } else if (')' == token) {
                while (!stack.isEmpty() && stack.peek() != '(') {
                    output.add(stack.pop())
                }
                stack.pop()
            } else {
                output.add(token)
            }
        }

        while (!stack.isEmpty()) {
            output.add(stack.pop())
        }

        return output
    }
}

class PolishSolver(private var token: String) {
    private val stack = Stack<Double>()
    private var num1 = 0.0
    private var num2 = 0.0

    fun solve(): Double {
        for (char in token) {
            if (char !in "+-*/^%") {
                try {
                    stack.push(char.digitToInt().toDouble())
                } catch(e: Exception) {
                    println("Converting wrong char to Digit! Error: $e")
                }
            } else {
                num2 = stack.pop()
                num1 = stack.pop()

                when(char) {
                    '+' -> stack.push(num1 + num2)
                    '-' -> stack.push(num1 - num2)
                    '*' -> stack.push(num1 * num2)
                    '/' -> stack.push(num1 / num2)
                    '%' -> stack.push(num1 % num2)
                    '^' -> stack.push(num1.pow(num2))
                }
            }
        }
        return stack.pop()
    }
}

fun main(args: Array<String>) {
    val reader = Scanner(System.`in`)
    var operation = ""

    do {
        println("Enter operation(or exit): ")
        operation = reader.nextLine() ?: ""
        if (operation == "exit" || operation == "") return

        val operands = operation.split("+", "-", "*", "/", "^", "%", "(", ")")
        val correctOperands = operands.filter {
            // Filter input to be a single-digit number
            it.length == 1 && it[0].isDigit() || it.isEmpty()
        }
        if (operands != correctOperands) {
            print("Wrong operation! Retry. ")
            continue
        }
        // Operation is correct
        val polish = ShuntingYard.shuntingYard(operation.toList()).joinToString("")
        val solved = PolishSolver(polish).solve()
        print("Polish notation: $polish")
        println(" = $solved")
    } while (true)
}