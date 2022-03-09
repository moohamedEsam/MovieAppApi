package com.example.movieappapi

import android.util.Log
import org.junit.Assert
import org.junit.Test
import java.text.SimpleDateFormat

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {
    @Test
    fun `parse date`() {
        try {
            val format = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
            format.parse("2022-01-01 15:01:02")
        } catch (exception: Exception) {
            Log.e("ExampleUnitTest", "parse date: ${exception.message}")
            Assert.fail()
        }
    }
}