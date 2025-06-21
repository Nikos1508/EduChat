package com.example.educhat.ui.item

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.educhat.ui.theme.EduChatTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(modifier: Modifier) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(MaterialTheme.colorScheme.background, MaterialTheme.colorScheme.secondary)
                )
            )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(32.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Icon(
                imageVector = Icons.Default.Person,
                contentDescription = "User Icon",
                tint = Color.White,
                modifier = Modifier
                    .size(100.dp)
                    .padding(bottom = 24.dp)
            )

            OutlinedTextField(
                value = "",
                onValueChange = {},
                label = { Text("Username", color = Color.White) },
                singleLine = true,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    cursorColor = Color.White,
                    focusedBorderColor = Color.White,
                    unfocusedBorderColor = Color.White

                    /*
                        focusedTextColor = COMPILED_CODE,
                        unfocusedTextColor = COMPILED_CODE,
                        disabledTextColor = COMPILED_CODE,
                        errorTextColor = COMPILED_CODE,
                        focusedContainerColor = COMPILED_CODE,
                        unfocusedContainerColor = COMPILED_CODE,
                        disabledContainerColor = COMPILED_CODE,
                        errorContainerColor = COMPILED_CODE,
                        errorCursorColor = COMPILED_CODE,
                        selectionColors = COMPILED_CODE,
                        disabledBorderColor = COMPILED_CODE,
                        errorBorderColor = COMPILED_CODE,
                        focusedLeadingIconColor = COMPILED_CODE,
                        unfocusedLeadingIconColor = COMPILED_CODE,
                        disabledLeadingIconColor = COMPILED_CODE,
                        errorLeadingIconColor = COMPILED_CODE,
                        focusedTrailingIconColor = COMPILED_CODE,
                        unfocusedTrailingIconColor = COMPILED_CODE,
                        disabledTrailingIconColor = COMPILED_CODE,
                        errorTrailingIconColor = COMPILED_CODE,
                        focusedLabelColor = COMPILED_CODE,
                        unfocusedLabelColor = COMPILED_CODE,
                        disabledLabelColor = COMPILED_CODE,
                        errorLabelColor = COMPILED_CODE,
                        focusedPlaceholderColor = COMPILED_CODE,
                        unfocusedPlaceholderColor = COMPILED_CODE,
                        disabledPlaceholderColor = COMPILED_CODE,
                        errorPlaceholderColor = COMPILED_CODE,
                        focusedSupportingTextColor = COMPILED_CODE,
                        unfocusedSupportingTextColor = COMPILED_CODE,
                        disabledSupportingTextColor = COMPILED_CODE,
                        errorSupportingTextColor = COMPILED_CODE,
                        focusedPrefixColor = COMPILED_CODE,
                        unfocusedPrefixColor = COMPILED_CODE,
                        disabledPrefixColor = COMPILED_CODE,
                        errorPrefixColor = COMPILED_CODE,
                        focusedSuffixColor = COMPILED_CODE,
                        unfocusedSuffixColor = COMPILED_CODE,
                        disabledSuffixColor = COMPILED_CODE,
                        errorSuffixColor = COMPILED_CODE
                    */
                    )
            )

            OutlinedTextField(
                value = "",
                onValueChange = {},
                label = { Text("Password", color = Color.White) },
                singleLine = true,
                visualTransformation = PasswordVisualTransformation(),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    cursorColor = Color.White,
                    focusedBorderColor = Color.White,
                    unfocusedBorderColor = Color.White
                )
            )

            Button(
                onClick = {},
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 16.dp)
                    .height(50.dp),
                shape = RoundedCornerShape(25.dp),
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primaryContainer)
            ) {
                Text("Login", color = Color.Black, fontSize = 16.sp)
            }

            TextButton(onClick = {}) {
                Text("Sign Up", color = Color.White.copy(alpha = 0.8f))
            }
        }
    }
}


    @Preview(showBackground = true)
@Composable
fun LoginScreenLightTheme() {
    EduChatTheme(darkTheme = false) {
        LoginScreen(modifier = Modifier)
    }
}

@Preview(showBackground = true)
@Composable
fun LoginScreenDarkTheme() {
    EduChatTheme(darkTheme = true) {
        LoginScreen(modifier = Modifier)
    }
}