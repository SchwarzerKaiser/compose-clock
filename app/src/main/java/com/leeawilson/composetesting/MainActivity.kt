package com.leeawilson.composetesting

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Text
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Radius
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.withTransform
import androidx.compose.ui.platform.setContent
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.lifecycleScope
import androidx.ui.tooling.preview.Preview
import com.leeawilson.composetesting.FingerId.*
import com.leeawilson.composetesting.ui.ComposeTestingTheme
import kotlinx.coroutines.*

private const val TAG = "MainActivity"

enum class FingerId {
    SECOND, MINUTE, HOUR
}

class MainActivity : AppCompatActivity() {

    private var _time = MutableLiveData<Time>()
    private val time: LiveData<Time> get() = _time

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ComposeTestingTheme {
                val timeState = time.observeAsState()
                Clock(timeState.value)
            }
        }

        lifecycleScope.launch {
            while (isActive) {
                val timeInstance = getTime()
                _time.value = timeInstance
                delay(1000)
            }
        }
    }
}

@Composable
fun Clock(time: Time? = null) {

    Canvas(modifier = Modifier.fillMaxSize()) {

        val canvasWidth = size.width
        val canvasHeight = size.height

        // Draw hours to the canvas
        for (hour in 0..330 step 30) {
            val squareSize = Size(18f, 60f)
            withTransform({
                rotate(degrees = hour.toFloat(), center)
                translate(0f, (0 - canvasWidth / 2.3f))
            }) {
                drawRoundRect(
                    alpha = 0.3f,
                    color = Color.LightGray,
                    size = squareSize,
                    topLeft = Offset(
                        x = (canvasWidth / 2) - squareSize.width / 2,
                        y = (canvasHeight / 2) - squareSize.height / 2
                    ),
                    radius = Radius(6f, 6f)
                )
            }
        }

        time?.let { time ->
            val secondFingerDimens = Size(3f, 400f)
            drawClockFinger(secondFingerDimens, SECOND, time)

            val minuteFingerDimens = Size(10f, 330f)
            drawClockFinger(minuteFingerDimens, MINUTE, time)

            val hourFingerDimens = Size(10f, 240f)
            drawClockFinger(hourFingerDimens, HOUR, time)
        }
    }
}

fun DrawScope.drawClockFinger(
    fingerSize: Size,
    fingerId: FingerId,
    time: Time
) {
    val angle = when (fingerId) {
        SECOND -> time.second.toFloat() * 6f
        MINUTE -> time.minute.toFloat() * 6f
        HOUR -> (time.hour.toFloat() * 30f) + ((time.minute.toFloat() * 6f) / 12f)
    }

    withTransform({
        rotate(degrees = angle, center)
        translate(0f, 0 - ((fingerSize.height / 2) + 60f))
    }) {
        drawRoundRect(
            color = Color.LightGray,
            size = fingerSize,
            topLeft = Offset(
                x = (size.width / 2) - fingerSize.width / 2,
                y = (size.height / 2) - fingerSize.height / 2
            ),
            radius = Radius(6f, 6f)
        )
    }
}