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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.fittracker.ui.theme.ButtonsGreen

@Composable
fun ProfileCard(
    icon: ImageVector,
    value: String,
    label: String,
    modifierCard: Modifier = Modifier,
    modifierIcon: Modifier = Modifier.size(24.dp)
){
    Card(
        modifier = modifierCard,
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
                modifier = modifierIcon,
                imageVector = icon,
                contentDescription = label,
                tint = ButtonsGreen
            )

            Text(
                text = value,
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold
            )

            Text(
                text = label,
                fontSize = 11.sp
            )
        }
    }
}