package com.example.educhat.ui.item

import android.Manifest
import android.os.Build
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.educhat.SupabaseAuthViewModel
import com.example.educhat.data.model.UserState

@Composable
fun SignUpScreen(
    viewModel: SupabaseAuthViewModel,
    onSignUpComplete: () -> Unit,
    onBackToLogin: () -> Unit
) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    val context = LocalContext.current
    val userStateValue = viewModel.userState.value

    var signUpAttemptMade by remember { mutableStateOf(false) }

    val requestPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        // Handle permission result if needed
    }

    LaunchedEffect(userStateValue, signUpAttemptMade) {  // Add signUpAttemptMade to the dependency list
        if (signUpAttemptMade) {
            when (userStateValue) {
                is UserState.Success -> {  // Handle success state
                    if (userStateValue.message == "Registered successfully!") {
                        Toast.makeText(context, "Registration successful!", Toast.LENGTH_SHORT).show()

                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                            requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
                        }

                        signUpAttemptMade = false
                        onSignUpComplete()
                    }
                }
                is UserState.Error -> {
                    Toast.makeText(context, "Sign Up Failed: ${userStateValue.message}", Toast.LENGTH_LONG).show()
                    signUpAttemptMade = false
                }
                UserState.Loading -> {
                    // Do nothing, show loading indicator
                }
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.Center
    ) {
        Text("Sign Up", style = MaterialTheme.typography.headlineMedium)

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Email") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Password") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                if (email.isNotBlank() && password.isNotBlank()) {
                    signUpAttemptMade = true
                    viewModel.signUp(context, email, password)
                } else {  // Handle empty email or password
                    Toast.makeText(context, "Email and password cannot be empty", Toast.LENGTH_SHORT).show()
                }
            },
            modifier = Modifier.fillMaxWidth(),
            enabled = userStateValue != UserState.Loading || !signUpAttemptMade
        ) {
            if (userStateValue == UserState.Loading && signUpAttemptMade) {  // Show loading indicator when loading
                CircularProgressIndicator(modifier = Modifier.height(24.dp), color = MaterialTheme.colorScheme.onPrimary)
            } else {  // Display "Create Account" when not loading
                Text("Create Account")
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        TextButton(  // Add a "Back to Login" button
            onClick = { onBackToLogin() },
            modifier = Modifier.align(Alignment.End)
        ) {
            Text("Already have an account? Log in")
        }
    }
}