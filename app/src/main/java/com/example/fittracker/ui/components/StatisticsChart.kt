package com.example.fittracker.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.unit.dp
import com.example.fittracker.data.DataPoint
import com.example.fittracker.ui.theme.ButtonsGreen

@Composable
fun StatisticsChart(
    data: List<DataPoint>,
    modifier: Modifier = Modifier
){
    if (data.isEmpty()) {
        Text("No data for selected filters!")
        return
    }

    val maxValue = data.maxOfOrNull { it.value } ?: 1f //cant really be null coz data is not empty

    Canvas(modifier = modifier) {
        val canvasWidth = size.width
        val canvasHeight = size.height

        val distanceBetweenPoints = canvasWidth / (data.size - 1).coerceAtLeast(1)

        for (i in 0 until data.size - 1){
            val startX = i * distanceBetweenPoints
            val startY = canvasHeight - (data[i].value / maxValue * canvasHeight) //the height has to be inverted to look naturally, coz in canvas y increases downside
            val endX = (i + 1) * distanceBetweenPoints
            val endY = canvasHeight - (data[i+1].value / maxValue * canvasHeight)

            drawLine(
                color = ButtonsGreen,
                start = Offset(startX, startY), //offset are just coordinates (distance from left, distance from top)
                end = Offset(endX, endY),
                strokeWidth = 2.dp.toPx() //converting dp to pixels
            )
        }

        data.forEachIndexed { index, point ->
            val x = index * distanceBetweenPoints
            val y = canvasHeight - (point.value / maxValue * canvasHeight)

            drawCircle(
                color = ButtonsGreen,
                radius = 5.dp.toPx(),
                center = Offset(x, y)
            )
        }

    }

}