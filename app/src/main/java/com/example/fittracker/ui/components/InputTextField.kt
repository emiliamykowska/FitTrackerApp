package com.example.fittracker.ui.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp


@Composable
fun InputTextField(
    value: String, // currently displayed text, it updates on onValueChange
    onValueChange: (String) -> Unit = {}, // function to update the value, unit means it doesn't return anything
    label: String = "",// a hint for a user what to enter
    fontSize: TextUnit = 16.sp,
    isPassword: Boolean = false
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        placeholder = { Text(text = label, fontSize = fontSize) }, // render a Text composable inside the OutlinedTextField (label)
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 1.dp),
        textStyle = TextStyle(fontSize = fontSize),
        visualTransformation = if (isPassword) PasswordVisualTransformation() else VisualTransformation.None,
        // to hide the password
        keyboardOptions = KeyboardOptions(keyboardType = if (isPassword) KeyboardType.Password else KeyboardType.Text)
        // to not show the hints when typing the password
    )
}
