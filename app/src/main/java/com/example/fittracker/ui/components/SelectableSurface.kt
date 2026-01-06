package com.example.fittracker.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.fittracker.ui.theme.ButtonsGreen
import com.example.fittracker.ui.theme.DarkGreen

@Composable
fun SelectableSurface(
    label: String,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
){
    Surface(
        modifier = modifier
            .clip(RoundedCornerShape(15.dp)) //for animation to also be rounded
            .clickable { onClick() },
        shape = RoundedCornerShape(15.dp),
        color = if (isSelected) ButtonsGreen.copy(alpha = 0.2f) else Color.White,
        border = BorderStroke(
            width = 1.dp,
            color = if (isSelected) ButtonsGreen else Color.LightGray
        )
    ) {
        Box(
            modifier = Modifier.padding(10.dp),
                contentAlignment = Alignment.Center
        ){
            Text(
                text = label,
                color = if (isSelected) DarkGreen else Color.Black,
                fontSize = 14.sp
            )
        }
    }
}