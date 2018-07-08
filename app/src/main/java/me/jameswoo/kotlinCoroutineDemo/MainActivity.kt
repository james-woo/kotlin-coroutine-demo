package me.jameswoo.kotlinCoroutineDemo

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.SeekBar
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.experimental.Job
import kotlinx.coroutines.experimental.android.UI
import kotlinx.coroutines.experimental.delay
import kotlinx.coroutines.experimental.launch
import java.util.Random

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
            startRunning(seekBarRed)
        }

        green = launch(UI) {
            startRunning(seekBarGreen)
        }

        blue = launch(UI) {
            startRunning(seekBarBlue)
        }
    }

    private fun resetRace() {
        end = false
        red?.cancel()
        green?.cancel()
        blue?.cancel()
    }

    private suspend fun startRunning(seekBar: SeekBar) {
        seekBar.progress = 0

        while (seekBar.progress < 1000 && !end) {
            delay(10) // coroutine delay does not block thread
            seekBar.progress += (1..10).random()
        }

        if (!end) {
            end = true
            Toast.makeText(this, "${seekBar.tooltipText} won!", Toast.LENGTH_SHORT).show()
        }
    }

    private fun ClosedRange<Int>.random() = Random().nextInt(endInclusive - start) + start
}
