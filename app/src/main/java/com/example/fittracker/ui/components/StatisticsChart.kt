package com.example.fittracker.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.fittracker.data.DataPoint
import com.example.fittracker.ui.screens.Metric
import com.example.fittracker.ui.theme.ButtonsGreen


@Composable
fun StatisticsChart(
    data: List<DataPoint>,
    selectedMetric: Metric,
    modifier: Modifier = Modifier
){
    if (data.isEmpty()) {
        Text("No data for selected filters!")
        return
    }

    val maxValue = data.maxOfOrNull { it.value }?.takeIf { it > 0f } ?: 1f //cant really be null coz data is not empty, if there is no data in this period takeIf to not divide by 0
    val textMeasurer = rememberTextMeasurer() //measures how much space in pixels each letter takes
    val textStyle = TextStyle(fontSize = 8.sp, color = Color.Gray)

    val yAxisUnit = if (selectedMetric == Metric.Duration) "Duration (h)" else "Count"

    Canvas(modifier = modifier.padding(8.dp)) {
        val canvasWidth = size.width
        val canvasHeight = size.height

        val labelWidth = 25.dp.toPx() // space for axis legend

        val chartWidth = canvasWidth - labelWidth
        val chartHeight = canvasHeight - labelWidth

        val distanceBetweenPoints = chartWidth / (data.size - 1).coerceAtLeast(1)

        drawText( //yAxis Unit label
            textMeasurer = textMeasurer,
            text = yAxisUnit,
            style = textStyle.copy(fontWeight = FontWeight.Bold),
            topLeft = Offset(0f, -20.dp.toPx()) //top left corner of the text is slightly above the chart
        )

        val yLines = 4
        repeat(yLines + 1){ i ->
            val yValue = (maxValue / yLines) * i
            val yPos = chartHeight - (yValue / maxValue * chartHeight)
            val yTextValue = "%.1f".format(yValue)
            val textHeight = textMeasurer.measure(yTextValue, textStyle).size.height

            drawLine(
                color = Color.LightGray.copy(alpha = 0.5f),
                start = Offset(labelWidth, yPos),
                end = Offset(canvasWidth, yPos),
                strokeWidth = 1.dp.toPx()
            )

            drawText(
                textMeasurer = textMeasurer,
                text = yTextValue,
                style = textStyle,
                topLeft = Offset(0f, yPos - (textHeight / 2))
            )
        }


        repeat(data.size){ i ->
            val isLast = i == data.size - 1
            val isEverySecond = i % 2 == 0
            val isCloseToLast = (data.size - 1) - i <= 1

            if (isEverySecond && !isCloseToLast || isLast || i == 0){ //show label for each third value
                val xPos = labelWidth + distanceBetweenPoints * i
                val textWidth = textMeasurer.measure(data[i].label, textStyle).size.width
                val xOffset = if (isLast) xPos - textWidth else xPos - (textWidth / 2) //if it's last justify to left

                drawText(
                    textMeasurer = textMeasurer,
                    text = data[i].label,
                    style = textStyle,
                    topLeft = Offset(xOffset, chartHeight + 10.dp.toPx())
                )
            }
        }


        for (i in 0 until data.size - 1){
            val startX = labelWidth + (i * distanceBetweenPoints) //shifted by labelWidth
            val startY = chartHeight - (data[i].value / maxValue * chartHeight) //the height has to be inverted to look naturally, coz in canvas y increases downside
            val endX = labelWidth + ((i + 1) * distanceBetweenPoints)
            val endY = chartHeight - (data[i+1].value / maxValue * chartHeight)

            drawLine(
                color = ButtonsGreen,
                start = Offset(startX, startY), //offset are just coordinates (distance from left, distance from top)
                end = Offset(endX, endY),
                strokeWidth = 2.dp.toPx() //converting dp to pixels
            )
        }

        data.forEachIndexed { index, point ->
            val x = labelWidth + (index * distanceBetweenPoints)
            val y = chartHeight - (point.value / maxValue * chartHeight)

            drawCircle(
                color = ButtonsGreen,
                radius = 5.dp.toPx(),
                center = Offset(x, y)
            )
        }

    }

}