package com.example.fittracker.ui.screens

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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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
import androidx.compose.foundation.background
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.ui.graphics.Brush
import com.example.fittracker.ui.theme.ButtonsGreen
import com.example.fittracker.ui.theme.DarkGreen
import com.example.fittracker.ui.theme.LightGreen
import com.google.firebase.auth.userProfileChangeRequest
import androidx.compose.ui.platform.LocalContext

@Composable

fun RegisterScreen(onNavigate: (String) -> Unit) { //function doesnt take anything and returns Unit (void)
    var email by remember { mutableStateOf("") } // mutableStateOf wraps the initial value ("" here, so like a blank paper) into MutableState
    // remember allows to remember this state
    var password by remember { mutableStateOf("") }
    var passwordConfirmation by remember {mutableStateOf("")}
    var name by remember { mutableStateOf("") }
    val auth = Firebase.auth // firebase initialization
    val context = LocalContext.current // same as this@RegisterActivity, but that does not exist in compose

    val scrollState = rememberScrollState()

    Column (
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .background(Brush.verticalGradient(
                colors = listOf(LightGreen, DarkGreen))),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ){

        Spacer(modifier = Modifier.height(15.dp))

        Logo()

        Text(
            text = "FitTracker",
            color = Color.White,
            fontSize = 22.sp,
            fontWeight = FontWeight.SemiBold,
            textAlign = TextAlign.Center
        )

        Text(
            text = "Track your fitness journey",
            color = Color.White,
            fontSize = 17.sp,
            fontWeight = FontWeight.Thin,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(5.dp))

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
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ){
                Text(
                    text = "Create Account",
                    modifier = Modifier.fillMaxWidth(),
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Left)

                Text(
                    text = "Name",
                    modifier = Modifier.fillMaxWidth(),
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Normal,
                    textAlign = TextAlign.Left)

                InputTextField(
                    value = name,
                    onValueChange = { newValue -> name = newValue.filter { it != '\n' } },
                    label = "John Doe"
                )

                Text(
                    text = "Email",
                    modifier = Modifier.fillMaxWidth(),
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Normal,
                    textAlign = TextAlign.Left)

                InputTextField(
                    value = email, //display email.value
                    onValueChange = { newValue -> email = newValue.filter { it != '\n' } }, //onValueChange = { newValue -> email.value = newValue }
                    label = "you@example.com"
                )

                Text(
                    text = "Password",
                    modifier = Modifier.fillMaxWidth(),
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Normal,
                    textAlign = TextAlign.Left,
                )

                InputTextField(
                    value = password,
                    onValueChange = { newValue -> password = newValue.filter { it != '\n' } },
                    label = "password",
                    isPassword = true
                )

                Text(
                    text = "Confirm your password",
                    modifier = Modifier.fillMaxWidth(),
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Normal,
                    textAlign = TextAlign.Left,
                )

                InputTextField(
                    value = passwordConfirmation,
                    onValueChange = { newValue -> passwordConfirmation = newValue.filter { it != '\n' }  },
                    label = "password",
                    isPassword = true
                )

                RoundedButton(
                    text="Create Account",
                    enabled = name.isNotBlank() && email.isNotBlank() && password.isNotBlank() && passwordConfirmation.isNotBlank(),
                    onClick = {
                        val cleanEmail = email.trim()
                        val cleanPassword = password.trim()
                        val cleanPasswordConfirmation = passwordConfirmation.trim()
                        val cleanName = name.trim()


                        when {
                            cleanName.length < 2 || cleanName.length > 50 -> {
                                Toast.makeText(context, "Name should be between 2 and 50 characters", Toast.LENGTH_SHORT).show()
                            }
                            !cleanEmail.contains("@") -> {
                                Toast.makeText(context, "Email has to contain '@'!", Toast.LENGTH_SHORT).show()
                            }
                            cleanPassword.length < 6 ->  {
                                Toast.makeText(context, "Password must contain at least 6 signs!",
                                    Toast.LENGTH_SHORT).show()
                            }
                            !cleanPassword.any { it.isLetter() } -> {
                                Toast.makeText(context, "Password must contain at least one letter!", Toast.LENGTH_SHORT).show()
                            }
                            !cleanPassword.any { it.isDigit() } -> {
                                Toast.makeText(context, "Password must contain at least one number!", Toast.LENGTH_SHORT).show()
                            }
                            cleanPassword != cleanPasswordConfirmation -> {
                                Toast.makeText(context, "Provided passwords are different!", Toast.LENGTH_SHORT).show()
                            }
                        else -> {
                            auth.createUserWithEmailAndPassword(cleanEmail, cleanPassword)
                                .addOnCompleteListener { task -> //task is an asynchronous object, so we call next things only AFTER executing the task in firebase
                                    if (task.isSuccessful) {
                                        val user = auth.currentUser //object of just created user
                                        val profileUpdates = userProfileChangeRequest {
                                            displayName = cleanName
                                        } //create profileUpdate object with changes

                                        user?.updateProfile(profileUpdates) //send a request to update profile of this user with profileUpdates object containing a request to name the user "cleanName"
                                            ?.addOnCompleteListener { updateTask ->
                                                if (updateTask.isSuccessful) {
                                                    Toast.makeText(
                                                        context,
                                                        "Successfully registered!",
                                                        Toast.LENGTH_SHORT
                                                    ).show()
                                                } else {
                                                    Toast.makeText(
                                                        context,
                                                        "Successfully registered, but there was a problem adding your name",
                                                        Toast.LENGTH_SHORT
                                                    ).show()
                                                }
                                                onNavigate("home")
                                            }
                                    } else {
                                        val errorText = task.exception?.localizedMessage
                                            ?: "An unknown error occurred"//localized message gives human readable reason for error
                                        Toast.makeText(context, errorText, Toast.LENGTH_LONG).show()
                                    }
                                }
                        }
                        }



                    }
                )


                Text(
                    text = "Already have an account?",
                    modifier = Modifier
                        .clickable { onNavigate("login") },
                    fontSize = 15.sp,
                    fontWeight = FontWeight.SemiBold,
                    textAlign = TextAlign.Left,
                    color = ButtonsGreen
                )
            }
        }
    }
}
