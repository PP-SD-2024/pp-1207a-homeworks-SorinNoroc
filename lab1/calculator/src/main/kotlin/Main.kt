import java.lang.Math.pow
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

internal object ShuntingYard {
    private val OPS: MutableMap<Char, Operator> = HashMap()

    init {
        // We build a map with all the existing Operators by iterating over the existing Enum
        // and filling up the map with:
        // <K,V> = <Character, Operator(Character, Associativity, Precedence)>
        for (operator in Operator.values()) {
            OPS[operator.symbol] = operator
        }
    }

    fun shuntingYard(tokens: List<Char>): List<Char> {
        val output: MutableList<Char> = LinkedList()
        val stack = Stack<Char>()

        // For all the input tokens [S1] read the next token [S2]
        for (token in tokens) {
            if (OPS.containsKey(token)) {
                // Token is an operator [S3]
                while (!stack.isEmpty() && OPS.containsKey(stack.peek())) {
                    // While there is an operator (y) at the top of the operators stack and
                    // either (x) is left-associative and its precedence is less or equal to
                    // that of (y), or (x) is right-associative and its precedence
                    // is less than (y)
                    //
                    // [S4]:
                    val cOp = OPS[token] // Current operator
                    val lOp = OPS[stack.peek()] // Top operator from the stack
                    if ((cOp!!.associativity == Associativity.LEFT && cOp.comparePrecedence(lOp!!) <= 0) ||
                        (cOp.associativity == Associativity.RIGHT && cOp.comparePrecedence(lOp!!) < 0)
                    ) {
                        // Pop (y) from the stack S[5]
                        // Add (y) output buffer S[6]
                        output.add(stack.pop())
                        continue
                    }
                    break
                }
                // Push the new operator on the stack S[7]
                stack.push(token)
            } else if ('(' == token) {
                // Else If token is left parenthesis, then push it on the stack S[8]
                stack.push(token)
            } else if (')' == token) {
                // Else If the token is right parenthesis S[9]
                while (!stack.isEmpty() && stack.peek() != '(') {
                    // Until the top token (from the stack) is left parenthesis, pop from
                    // the stack to the output buffer
                    // S[10]
                    output.add(stack.pop())
                }
                // Also pop the left parenthesis but don't include it in the output
                // buffer S[11]
                stack.pop()
            } else {
                // Else add token to output buffer S[12]
                output.add(token)
            }
        }

        while (!stack.isEmpty()) {
            // While there are still operator tokens in the stack, pop them to output S[13]
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

        val operands = operation.split("+", "-", "*", "/", "^", "%")
        val correctOperands = operands.filter {
            // Filter input to be a single-digit number
            it.length == 1 && it[0].isDigit()
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