package com.example.fittracker.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp

@Composable
fun HeaderStatCard(
    label: String,
    value: String,
    modifier: Modifier = Modifier){

    Card(
        modifier = modifier.height(65.dp),
        shape = RoundedCornerShape(15.dp),
        colors = CardDefaults.cardColors(Color.White.copy(alpha = 0.2f)) // for card to be partially transparent
    ){
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceEvenly
        ){
            Text(
                text = value,
                color = Color.White,
                fontWeight = FontWeight.Bold,
                fontSize = 15.sp
            )

            Text(
                text = label,
                color = Color.White,
                fontWeight = FontWeight.Thin,
                fontSize = 11.sp,
                textAlign = TextAlign.Center // text alignment to the center
            )

        }
    }
}