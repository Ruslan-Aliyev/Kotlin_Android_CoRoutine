package com.example.coroutine

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val button = findViewById(R.id.button) as Button

        button.setOnClickListener {
            CoroutineScope(IO).launch {
                fakeApiRequest()
            }
        }
    }

    private suspend fun fakeApiRequest() {
        logThread("fakeApiRequest")

        val result1 = getResult1FromApi() // wait until job is done
        setTextOnMainThread("Got $result1")
        val result2 = getResult2FromApi() // wait until job is done
        setTextOnMainThread("Got $result2")
    }

    private suspend fun getResult1FromApi(): String {
        logThread("getResult1FromApi")
        delay(1000) // Does not block thread. Just suspends the coroutine inside the thread
        return "Result #1"
    }
    private suspend fun getResult2FromApi(): String {
        logThread("getResult2FromApi")
        delay(1000)
        return "Result #2"
    }

    private suspend fun setTextOnMainThread(input: String) {
        withContext (Main) {
            setNewText(input)
        }
    }
    private fun setNewText(input: String) {
        val text = findViewById(R.id.text) as TextView
        val newText = text.text.toString() + "\n$input"
        text.text = newText
    }

    private fun logThread(methodName: String) {
        println("debug: ${methodName}: ${Thread.currentThread().name}")
    }
}