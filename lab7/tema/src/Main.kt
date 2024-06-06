import java.io.File
import java.sql.Timestamp

interface Commander {
    fun getCommand(): String
    fun setCommand(s: String)
}

class HistoryLogRecord(dateString: String, private var command: String): Comparable<Timestamp>, Commander {
    // 2024-04-03  22:57:17
    private var startDate: Timestamp
    init {
        val dateHour = dateString.split("  ")
        val date = dateHour[0].split("-").map { it.toInt() }
        val hours = dateHour[1].split(":").map { it.toInt() }

        startDate = Timestamp(date[0] - 1900, date[1] + 1, date[2], hours[0], hours[1], hours[2], 0)
    }

    fun getTimestamp() = startDate
    override fun getCommand() = command
    override fun setCommand(s: String) {
        command = s
    }

    override fun compareTo(other: Timestamp): Int {
        val diff = this.startDate.time - other.time
        return if (diff < 0) -1 else if (diff == 0L) 0 else 1
    }

    override fun toString(): String {
        return "HistoryLogRecord with Timestamp: ($startDate) and Command ($command)"
    }
}

fun <T: Comparable<T>>maximTimestamp(rec1: T, rec2: T): T {
    val comparation = rec1.compareTo(rec2)
    return if (comparation >= 0) rec1 else rec2
}

fun <U, T:Commander>findReplace(one: T, two: T, map: MutableMap<U, in T>) {
    for ((k, v) in map) {
        if (v == one) {
            map[k] = two
        }
    }
}

fun main() {
    val file = File("/var/log/apt/history.log")
    val last50 = file.readText().split("\n\n").takeLast(50)

    val logRecordList = mutableListOf<HistoryLogRecord>()
    last50.forEach { p ->
        p.split("\n").filter{it != ""}.subList(0,2).also { dateAndCommand ->
            logRecordList.add(
                HistoryLogRecord(
                    dateAndCommand[0].split(": ")[1],
                    dateAndCommand[1].split(": ")[1]
                )
            )
        }
    }

    val mapRecords: MutableMap<Timestamp, HistoryLogRecord> = logRecordList.associateBy { it.getTimestamp() }.toMutableMap()
    println("Max Timestamp: " + maximTimestamp(logRecordList[0].getTimestamp(), logRecordList[2].getTimestamp()))

    findReplace(logRecordList[0], logRecordList[1], mapRecords)
    for ((k, v) in mapRecords) {
        println("$k, $v")
    }

}
