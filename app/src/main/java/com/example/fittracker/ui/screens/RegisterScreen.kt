package com.example.fittracker.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.fittracker.ui.components.InputTextField
import com.example.fittracker.ui.components.RoundedButton
import com.example.fittracker.ui.components.Logo
import com.example.fittracker.ui.theme.DarkGreen
import com.example.fittracker.ui.theme.LightGreen


@Composable

fun RegisterScreen(onNavigateToLogin: () -> Unit) { //function doesnt take anything and returns Unit (void)
    val email = remember { mutableStateOf("") } // mutableStateOf wraps the initial value ("" here, so like a blank paper) into MutableState
    // remember allows to remember this state
    val password = remember { mutableStateOf("") }

    val name = remember {mutableStateOf("") }

    Column (
        modifier = Modifier
            .fillMaxSize()
            .padding(10.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ){
        Logo()

        Spacer(modifier = Modifier.height(10.dp))

        Text(
            text = "FitTracker",
            color = Color.White,
            fontSize = 22.sp,
            fontWeight = FontWeight.SemiBold,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(10.dp))

        Text(
            text = "Track your fitness journey",
            color = Color.White,
            fontSize = 17.sp,
            fontWeight = FontWeight.Thin,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(15.dp))

        Card (
            shape = RoundedCornerShape(
                30.dp
            ),
            modifier = Modifier
                .padding(10.dp)
        ){
            Column (
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ){
                Text(
                    text = "Create Account",
                    modifier = Modifier.fillMaxWidth(),
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Left)

                Spacer(modifier = Modifier.height(10.dp))

                Text(
                    text = "Name",
                    modifier = Modifier.fillMaxWidth(),
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Normal,
                    textAlign = TextAlign.Left)

                InputTextField(
                    value = name.value,
                    onValueChange = { name.value = it },
                    label = "John Doe"
                )

                Spacer(modifier = Modifier.height(10.dp))

                Text(
                    text = "Email",
                    modifier = Modifier.fillMaxWidth(),
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Normal,
                    textAlign = TextAlign.Left)

                InputTextField(
                    value = email.value, //display email.value
                    onValueChange = { email.value = it }, //onValueChange = { newValue -> email.value = newValue }
                    label = "you@example.com"
                )

                Spacer(modifier = Modifier.height(10.dp))

                Text(
                    text = "Password",
                    modifier = Modifier.fillMaxWidth(),
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Normal,
                    textAlign = TextAlign.Left,
                )

                InputTextField(
                    value = password.value,
                    onValueChange = { password.value = it },
                    label = "*****",
                    isPassword = true
                )

                Spacer(modifier = Modifier.height(10.dp))

                RoundedButton(text="Create Account")

                Spacer(modifier = Modifier.height(10.dp))

                Text(
                    text = "Already have an account?",
                    modifier = Modifier
                        .clickable { onNavigateToLogin() },
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Normal,
                    textAlign = TextAlign.Left
                )
            }
        }
    }
}
