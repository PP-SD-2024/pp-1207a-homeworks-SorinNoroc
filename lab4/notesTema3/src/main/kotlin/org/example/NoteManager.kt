package org.example

import java.io.File
import java.security.MessageDigest
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class NoteManager {
    val fileManager = FileManager
    val notes: List<Note> = mutableListOf()

    fun createNote(author: User, title: String, text: String): Note {
        val timeNow: String = LocalDate.now().format(
            DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm")
        )
        val note = Note(author, timeNow, title, text)
        return note
    }
}

data class Note(
    private val author: User,
    private val createTime: String,
    private val title: String,
    private val text: String
    )

class User(val name: String) {
    fun hashPass(password: String): String {
        val md = MessageDigest.getInstance("MD5")
        val digest: ByteArray = md.digest(password.toByteArray())
        return digest.joinToString(separator = "") { eachByte -> "%02x".format(eachByte) }
    }
}

object FileManager {
    fun loadNote(fileName: String): Note? {
        val content = File(fileName).readLines()
        val user = User(content[0])

        return null
    }
}