package com.example.fittracker.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.fittracker.ui.theme.ButtonsGreen

@Composable
fun ActivityCard(
    value: String,
    label: String,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
){
    Card(
        modifier = modifier //modifier from parameter
            .fillMaxWidth()
            .height(90.dp)
            .clickable() { onClick() },
        shape = RoundedCornerShape(15.dp),
        border = BorderStroke(1.dp, if(isSelected) ButtonsGreen else Color.Gray),
        colors = CardDefaults.cardColors(
            containerColor = if (isSelected) ButtonsGreen.copy(alpha = 0.2f) else Color.White
        )
    ){
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ){
            Text(
                text = value,
                fontSize = 24.sp
            )
            Text(
                text = label,
                fontSize = 16.sp
            )
        }
    }
}