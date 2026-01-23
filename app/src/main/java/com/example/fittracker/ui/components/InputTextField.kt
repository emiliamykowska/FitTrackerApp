package com.example.fittracker.ui.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.material3.Icon


@Composable
fun InputTextField(
    value: String, // currently displayed text, it updates on onValueChange
    onValueChange: (String) -> Unit = {}, // function to update the value, unit means it doesn't return anything
    label: String = "",// a hint for a user what to enter
    fontSize: TextUnit = 16.sp,
    isPassword: Boolean = false,
    keyboardType: KeyboardType = KeyboardType.Text // text by default
) {
    var isPasswordVisible by remember { mutableStateOf(false) }

    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        placeholder = { Text(text = label, fontSize = fontSize) }, // render a Text composable inside the OutlinedTextField (label)
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 1.dp),
        textStyle = TextStyle(fontSize = fontSize),
        visualTransformation =
            if (isPassword && !isPasswordVisible) {
                PasswordVisualTransformation()
            }
            else {
                VisualTransformation.None
            },
        // to hide the password
        keyboardOptions = KeyboardOptions(keyboardType = if (isPassword) KeyboardType.Password else keyboardType),
        // to not show the hints when typing the password
        trailingIcon = {
            if (isPassword){
                IconButton(
                    onClick = { isPasswordVisible = !isPasswordVisible }
                ) {
                    Icon(
                        imageVector = if (isPasswordVisible) Icons.Filled.Visibility else Icons.Filled.VisibilityOff,
                        contentDescription = if (isPasswordVisible) "Show password" else "Hide password"
                    )
                }
            }
        }
    )
}
