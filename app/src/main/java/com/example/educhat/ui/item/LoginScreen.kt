package com.example.educhat.ui.item

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.educhat.SupabaseAuthViewModel
import com.example.educhat.data.model.UserState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(
    onLoginSuccess: () -> Unit,
    onSignUpClick: () -> Unit,
    viewModel: SupabaseAuthViewModel
) {
    val userStateValue = viewModel.userState.value
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    val context = LocalContext.current
    var loginAttemptMade by remember { mutableStateOf(false) }

    LaunchedEffect(userStateValue, loginAttemptMade) {
        if (loginAttemptMade) {
            when (userStateValue) {
                is UserState.Success -> {
                    if (userStateValue.message == "Logged in successfully!") {
                        Toast.makeText(context, "Login Successful!", Toast.LENGTH_SHORT).show()
                        onLoginSuccess()
                    }
                }
                is UserState.Error -> {
                    Toast.makeText(context, "Login Failed: ${userStateValue.message}", Toast.LENGTH_LONG).show()
                    loginAttemptMade = false
                }
                UserState.Loading -> { /* Show progress in UI */ }
            }
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    listOf(
                        MaterialTheme.colorScheme.primary,
                        MaterialTheme.colorScheme.primaryContainer
                    )
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
                tint = MaterialTheme.colorScheme.onPrimaryContainer,
                modifier = Modifier
                    .size(100.dp)
                    .padding(bottom = 24.dp)
            )

            var emailFocused by remember { mutableStateOf(false) }
            Column {
                Text(
                    text = "Email",
                    fontSize = if (emailFocused || email.isNotEmpty()) 12.sp else 16.sp,
                     color = if (emailFocused) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.padding(bottom = if (emailFocused || email.isNotEmpty()) 4.dp else 8.dp)
                )

                BasicTextField(
                    value = email,
                    onValueChange = { email = it },
                    modifier = Modifier
                        .fillMaxWidth()
                        .onFocusChanged { emailFocused = it.isFocused },
                    textStyle = TextStyle(
                        color = MaterialTheme.colorScheme.onSurface,
                        fontSize = 16.sp
                    ),
                    singleLine = true,
                    cursorBrush = SolidColor(MaterialTheme.colorScheme.primary),
                    decorationBox = { innerTextField ->
                        Box(
                            Modifier.fillMaxWidth(),
                            contentAlignment = Alignment.CenterStart
                        ) {
                            if (email.isEmpty() && (emailFocused || email.isNotEmpty())) {
                                Text(
                                    "Enter your email",
                                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                                    fontSize = 16.sp
                                )
                            }
                            innerTextField()
                        }
                    }
                )
                HorizontalDivider(
                    thickness = if (emailFocused) 2.dp else 1.dp,
                    color = if (emailFocused) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.4f)
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            var passwordFocused by remember { mutableStateOf(false) }

            Column {
                Text(
                    text = "Password",
                    fontSize = if (passwordFocused || password.isNotEmpty()) 12.sp else 16.sp,
                    color = if (passwordFocused) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.padding(bottom = if (passwordFocused || password.isNotEmpty()) 4.dp else 8.dp)
                )

                BasicTextField(
                    value = password,
                    onValueChange = { password = it },
                    modifier = Modifier
                        .fillMaxWidth()
                        .onFocusChanged { passwordFocused = it.isFocused },
                    textStyle = TextStyle(
                        color = MaterialTheme.colorScheme.onSurface,
                        fontSize = 16.sp
                    ),
                    visualTransformation = PasswordVisualTransformation(),
                    singleLine = true,
                    cursorBrush = SolidColor(MaterialTheme.colorScheme.primary),
                    decorationBox = { innerTextField ->
                        Box(
                            Modifier.fillMaxWidth(),
                            contentAlignment = Alignment.CenterStart
                        ) {
                            if (password.isEmpty() && (passwordFocused || password.isNotEmpty())) {
                                Text(
                                    "Enter your password",
                                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                                    fontSize = 16.sp
                                )
                            }
                            innerTextField()
                        }
                    }
                )
                HorizontalDivider(
                    thickness = if (passwordFocused) 2.dp else 1.dp,
                    color = if (passwordFocused) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.4f)
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = {
                    if (email.isNotBlank() && password.isNotBlank()) {
                        loginAttemptMade = true
                        viewModel.login(context, email, password)
                    } else {
                        Toast.makeText(context, "Email and password cannot be empty", Toast.LENGTH_SHORT).show()
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                shape = RoundedCornerShape(25.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = MaterialTheme.colorScheme.onPrimary
                ),
                enabled = userStateValue != UserState.Loading || !loginAttemptMade
            ) {
                if (userStateValue == UserState.Loading && loginAttemptMade) {
                    CircularProgressIndicator(modifier = Modifier.size(24.dp), color = MaterialTheme.colorScheme.onPrimary)
                } else {
                    Text("Login")
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            TextButton(onClick = {
                // Just navigate to the SignUp screen, no sign-up logic here
                onSignUpClick()
            }) {
                Text("Sign Up")
            }

            Spacer(modifier = Modifier.height(16.dp))

            when (userStateValue) {
                is UserState.Loading -> {
                    if (!loginAttemptMade) {
                        // CircularProgressIndicator() // Might be redundant because MainActivity shows one
                    }
                }
                is UserState.Error -> {
                    if (!loginAttemptMade) {
                        Text(userStateValue.message, color = MaterialTheme.colorScheme.error)
                    }
                }
                is UserState.Success -> {
                    if (!loginAttemptMade &&
                        userStateValue.message != "Logged in successfully!" &&
                        userStateValue.message != "Registered successfully!" &&
                        userStateValue.message != "User already logged in!" &&
                        userStateValue.message != "User not logged in!") {
                        // Text(userStateValue.message, color = MaterialTheme.colorScheme.secondary)
                    }
                }
            }
        }
    }
}