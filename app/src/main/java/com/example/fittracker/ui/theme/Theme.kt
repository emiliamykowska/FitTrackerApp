package com.example.fittracker.ui.theme

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Shapes
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

private val FTColorScheme = lightColorScheme( //daily theme colors
    primary = ButtonsGreen, //color of the buttons
    onPrimary = Color.White, //color of the text on buttons
    surface = Color.White //color of other components, like card
)

private val FTShapes = Shapes(
    medium = RoundedCornerShape(8.dp), //input text fields
)

@Composable
fun FitTrackerTheme(content: @Composable () -> Unit) { //content is the whole application
    val colorScheme = FTColorScheme
    val shapes = FTShapes
    MaterialTheme(
        colorScheme = colorScheme,
        shapes = shapes,
        content = content //putting all screens here
    )
}