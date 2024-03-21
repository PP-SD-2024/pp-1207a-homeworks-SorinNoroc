package org.example

import org.junit.Test
import kotlin.test.assertEquals

class HelloTest {

    @Test
    fun `Calling extension md5 should return a md5 hash`() {
        val stringToBeHashed = "Hello, Baeldung!"
        val expectedHash = "6469a4ea9e2753755f5120beb51587f8"
        val calculatedHash = stringToBeHashed.hash()

        assertEquals(expectedHash, calculatedHash)
    }

}
