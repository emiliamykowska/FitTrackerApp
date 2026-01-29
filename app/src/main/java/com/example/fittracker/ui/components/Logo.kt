package com.example.fittracker.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.fittracker.R

@Composable
fun Logo(){
    Surface( //to ensure circular shape
        modifier = Modifier
            .size(70.dp)
            .padding(3.dp),

        shape = CircleShape,
        color = Color.White
    ){
    Image(
        painter = painterResource(id = R.drawable.logo_ft),
        contentDescription = "Logo"
    )
    }
}