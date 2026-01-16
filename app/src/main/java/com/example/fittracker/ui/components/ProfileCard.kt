package com.example.fittracker.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.fittracker.ui.theme.ButtonsGreen

@Composable
fun ProfileCard(
    icon: ImageVector,
    value: String,
    label: String,
    modifier: Modifier = Modifier,
    iconSize: Dp = (24.dp),
    valueFontSize: TextUnit = 15.sp,
    labelFontSize: TextUnit = 11.sp
){
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(15.dp)
    ){
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(4.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(5.dp)
        ){
            Icon(
                modifier = Modifier.size(iconSize),
                imageVector = icon,
                contentDescription = label,
                tint = ButtonsGreen
            )

            Text(
                text = value,
                fontSize = valueFontSize,
                fontWeight = FontWeight.Bold
            )

            Text(
                text = label,
                fontSize = labelFontSize
            )
        }
    }
}