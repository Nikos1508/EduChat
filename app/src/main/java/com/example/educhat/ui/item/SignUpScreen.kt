package com.example.educhat.ui.item

import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
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
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.educhat.R
import com.example.educhat.SupabaseAuthViewModel
import com.example.educhat.data.model.UserState

@Composable
fun SignUpScreen(
    viewModel: SupabaseAuthViewModel,
    onSignUpComplete: () -> Unit,
    onBackToLogin: () -> Unit
) {
    val context = LocalContext.current
    val userStateValue = viewModel.userState.value

    var displayName by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    var signUpAttemptMade by remember { mutableStateOf(false) }

    var emailFocused by remember { mutableStateOf(false) }
    var passwordFocused by remember { mutableStateOf(false) }
    var displayNameFocused by remember { mutableStateOf(false) }

    val requestPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        // Handle permission result if needed
    }

    val signUpFailedFormat = stringResource(R.string.sign_up_failed)

    LaunchedEffect(userStateValue, signUpAttemptMade) {
        if (signUpAttemptMade) {
            when (userStateValue) {
                is UserState.Success -> {
                    Toast.makeText(context, userStateValue.message, Toast.LENGTH_LONG).show()
                    if (userStateValue.message.contains("Check your email")) {
                        onSignUpComplete()
                    }
                    signUpAttemptMade = false
                }
                is UserState.Error -> {
                    Toast.makeText(
                        context,
                        String.format(signUpFailedFormat, userStateValue.message),
                        Toast.LENGTH_LONG
                    ).show()
                    signUpAttemptMade = false
                }
                UserState.Loading -> { }
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

            // Email Input
            Column(modifier = Modifier.fillMaxWidth()) {
                Text(
                    text = stringResource(R.string.email_label),
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
                                    stringResource(R.string.email_placeholder),
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

            // Password Input
            Column(modifier = Modifier.fillMaxWidth()) {
                Text(
                    text = stringResource(R.string.password_label),
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
                                    stringResource(R.string.password_placeholder),
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

            Spacer(modifier = Modifier.height(32.dp))

            // Display Name Input
            Column(modifier = Modifier.fillMaxWidth()) {
                Text(
                    text = stringResource(R.string.display_name_label),
                    fontSize = if (displayNameFocused || displayName.isNotEmpty()) 12.sp else 16.sp,
                    color = if (displayNameFocused) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.padding(bottom = if (displayNameFocused || displayName.isNotEmpty()) 4.dp else 8.dp)
                )

                BasicTextField(
                    value = displayName,
                    onValueChange = { displayName = it },
                    modifier = Modifier
                        .fillMaxWidth()
                        .onFocusChanged { displayNameFocused = it.isFocused },
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
                            if (displayName.isEmpty() && (displayNameFocused || displayName.isNotEmpty())) {
                                Text(
                                    stringResource(R.string.display_name_placeholder),
                                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                                    fontSize = 16.sp
                                )
                            }
                            innerTextField()
                        }
                    }
                )
                HorizontalDivider(
                    thickness = if (displayNameFocused) 2.dp else 1.dp,
                    color = if (displayNameFocused) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.4f)
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            val fillAllFields = stringResource(R.string.fill_all_fields)
            Button(
                onClick = {
                    if (email.isNotBlank() && password.isNotBlank() && displayName.isNotBlank()) {
                        signUpAttemptMade = true
                        viewModel.signUp(context, email, password, displayName) {
                            onBackToLogin()
                        }
                    } else {
                        Toast.makeText(context, fillAllFields, Toast.LENGTH_SHORT).show()
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
                enabled = userStateValue != UserState.Loading || !signUpAttemptMade
            ) {
                if (userStateValue == UserState.Loading && signUpAttemptMade) {
                    CircularProgressIndicator(modifier = Modifier.size(24.dp), color = MaterialTheme.colorScheme.onPrimary)
                } else {
                    Text(stringResource(R.string.create_account))
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            TextButton(onClick = { onBackToLogin() }) {
                Text(stringResource(R.string.already_have_account))
            }
        }
    }
}