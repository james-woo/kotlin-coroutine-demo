package me.jameswoo.kotlin_coroutine_demo

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.ProgressBar
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.experimental.Job
import kotlinx.coroutines.experimental.android.UI
import kotlinx.coroutines.experimental.delay
import kotlinx.coroutines.experimental.launch
import java.util.Random
import kotlinx.android.synthetic.main.activity_main.start_button as startButton

class MainActivity : AppCompatActivity() {

    var red: Job? = null
    var green: Job? = null
    var blue: Job? = null
    var end = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        startButton.setOnClickListener {
            race()
        }
    }

    private fun race() {
        resetRace()

        red = launch(UI) {
            startRunning(progressBarRed)
        }

        green = launch(UI) {
            startRunning(progressBarGreen)
        }

        blue = launch(UI) {
            startRunning(progressBarBlue)
        }
    }

    private suspend fun startRunning(progressBar: ProgressBar) {
        progressBar.progress = 0

        while (progressBar.progress < 100 && !end) {
            delay(10) // coroutine delay does not block thread
            progressBar.progress += (1..10).random()
        }

        if (!end) {
            end = true
            Toast.makeText(this, "${progressBar.tooltipText} won!", Toast.LENGTH_SHORT).show()
        }
    }

    private fun resetRace() {
        end = false
        red?.cancel()
        green?.cancel()
        blue?.cancel()
    }

    private fun ClosedRange<Int>.random() = Random().nextInt(endInclusive - start) + start
}
