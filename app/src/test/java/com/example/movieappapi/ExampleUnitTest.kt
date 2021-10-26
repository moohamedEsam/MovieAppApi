package com.example.movieappapi

import org.junit.Test
import java.text.DateFormat
import java.util.*

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {
    @Test
    fun `parse date`() {
        val result = DateFormat.getInstance().parse("2021-8-27")
        assert(result.before(Date()))
    }
}