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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.fittracker.ui.components.InputTextField
import com.example.fittracker.ui.components.RoundedButton
import com.example.fittracker.ui.components.Logo
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import android.widget.Toast
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Brush
import com.example.fittracker.ui.theme.ButtonsGreen
import com.example.fittracker.ui.theme.DarkGreen
import com.example.fittracker.ui.theme.LightGreen
import kotlin.text.trim
import androidx.compose.ui.platform.LocalContext


@Composable
fun LoginScreen(onNavigate: (String) -> Unit) {
    // dont have to provide a string when calling a function, it will be defined when user clicks some button
    var email by remember { mutableStateOf("") } // mutableStateOf wraps the initial value ("" here, so like a blank paper) into MutableState
    // remember allows to remember this state
    var password by remember { mutableStateOf("") }
    val auth = Firebase.auth //authentication
    val context = LocalContext.current

    val scrollState = rememberScrollState()


    Column (
        modifier = Modifier
            .fillMaxSize()
            .padding(10.dp)
            .verticalScroll(scrollState)
            .background(Brush.verticalGradient(
                colors = listOf(LightGreen, DarkGreen))),
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
                    text = "Welcome Back!",
                    modifier = Modifier.fillMaxWidth(),
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Left)

                Spacer(modifier = Modifier.height(10.dp))

                Text(
                    text = "Email",
                    modifier = Modifier.fillMaxWidth(),
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Normal,
                    textAlign = TextAlign.Left)

                InputTextField(
                    value = email, //display email.value
                    onValueChange = { email = it }, //onValueChange = { newValue -> email.value = newValue }
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
                    value = password,
                    onValueChange = { password = it },
                    label = "*****",
                    isPassword = true
                )

                Spacer(modifier = Modifier.height(10.dp))
                RoundedButton(
                    text="Sign In",
                    enabled = email.trim().isNotEmpty() && password.trim().isNotEmpty(),
                    onClick = {
                        val cleanEmail = email.trim()
                        val cleanPassword = password.trim()
                        auth.signInWithEmailAndPassword(cleanEmail, cleanPassword)
                            .addOnCompleteListener {
                                task -> if (task.isSuccessful) {
                                    onNavigate("home")
                                } else {
                                    val errorText = task.exception?.localizedMessage ?: "Unknown error occurred"
                                    Toast.makeText(context, errorText, Toast.LENGTH_LONG).show()
                                }
                            }
                    }
                )

                Spacer(modifier = Modifier.height(10.dp))

                Text(
                    text = "Don't have an account yet?",
                    modifier = Modifier
                        .clickable{onNavigate("register")},
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Normal,
                    textAlign = TextAlign.Left,
                    color = ButtonsGreen
                )
            }
        }
    }
}
