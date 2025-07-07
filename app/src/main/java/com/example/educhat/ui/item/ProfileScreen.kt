package com.example.educhat.ui.item

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.filled.LockReset
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.example.educhat.AppScreen
import com.example.educhat.R
import com.example.educhat.SupabaseAuthViewModel

@Composable
fun ProfileScreen(
    modifier: Modifier = Modifier,
    viewModel: SupabaseAuthViewModel,
    navController: NavController,
    onLogoutComplete: () -> Unit
) {
    val context = LocalContext.current
    val userEmail by viewModel.currentUserEmail
    val userProfile by viewModel.userProfile

    var showResetPasswordDialog by remember { mutableStateOf(false) }

    val logoutText = stringResource(R.string.logged_out)
    val noEmailFoundText = stringResource(R.string.no_email_found)

    val imagePainter = rememberAsyncImagePainter(
        model = userProfile?.profileImageUrl ?: R.drawable.profile_image
    )

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = imagePainter,
            contentDescription = stringResource(R.string.profile_picture),
            modifier = Modifier
                .size(100.dp)
                .clip(CircleShape)
                .border(2.dp, MaterialTheme.colorScheme.onBackground, CircleShape),
            contentScale = ContentScale.Crop
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = userProfile?.displayName ?: stringResource(R.string.unknown_user),
            style = MaterialTheme.typography.headlineSmall,
            color = MaterialTheme.colorScheme.onBackground,
            fontWeight = FontWeight.Bold
        )

        Text(
            text = userEmail ?: stringResource(R.string.unknown_email),
            style = MaterialTheme.typography.bodyMedium,
            color = Color.Gray
        )

        Spacer(modifier = Modifier.height(24.dp))

        HorizontalDivider(thickness = 2.dp, color = MaterialTheme.colorScheme.outlineVariant)

        ProfileOptionItem(
            icon = Icons.Default.Person,
            text = stringResource(R.string.account_info),
            onClick = { navController.navigate(AppScreen.EditProfile.name) }
        )

        HorizontalDivider(thickness = 2.dp, color = MaterialTheme.colorScheme.outlineVariant)

        ProfileOptionItem(
            icon = Icons.Default.Notifications,
            text = stringResource(R.string.notifications),
            onClick = { /* TODO */ }
        )

        HorizontalDivider(thickness = 2.dp, color = MaterialTheme.colorScheme.outlineVariant)

        ProfileOptionItem(
            icon = Icons.Default.LockReset,
            text = stringResource(R.string.reset_password),
            onClick = {
                showResetPasswordDialog = true
            }
        )

        HorizontalDivider(thickness = 2.dp, color = MaterialTheme.colorScheme.outlineVariant)

        ProfileOptionItem(
            icon = Icons.AutoMirrored.Filled.ExitToApp,
            text = stringResource(R.string.log_out),
            onClick = {
                viewModel.logout(context)
                Toast.makeText(context, logoutText, Toast.LENGTH_SHORT).show()
                onLogoutComplete()
            }
        )

        HorizontalDivider(thickness = 2.dp, color = MaterialTheme.colorScheme.outlineVariant)
    }

    if (showResetPasswordDialog) {
        ResetPasswordConfirmationDialog(
            onConfirm = {
                showResetPasswordDialog = false
                userEmail?.let {
                    viewModel.sendPasswordResetEmail(context, it)
                } ?: Toast.makeText(context, noEmailFoundText, Toast.LENGTH_SHORT).show()
            },
            onDismiss = {
                showResetPasswordDialog = false
            }
        )
    }
}

@Composable
fun ResetPasswordConfirmationDialog(
    onConfirm: () -> Unit,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(stringResource(R.string.reset_password)) },
        text = { Text(stringResource(R.string.reset_password_confirmation)) },
        confirmButton = {
            TextButton(onClick = onConfirm) {
                Text(stringResource(R.string.yes))
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(stringResource(R.string.no))
            }
        }
    )
}

@Composable
fun ProfileOptionItem(
    icon: ImageVector,
    text: String,
    onClick: () -> Unit = {}
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(vertical = 12.dp, horizontal = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.primary
        )
        Spacer(modifier = Modifier.width(16.dp))
        Text(
            text = text,
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}
