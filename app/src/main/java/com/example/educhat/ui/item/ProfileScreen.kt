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
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
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

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = rememberAsyncImagePainter(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(userProfile?.profileImageUrl)
                    .crossfade(true)
                    .error(R.drawable.profile_image)
                    .placeholder(R.drawable.profile_image)
                    .build()
            ),
            contentDescription = "Profile Picture",
            modifier = Modifier
                .size(100.dp)
                .clip(CircleShape)
                .border(2.dp, Color.Gray, CircleShape),
            contentScale = ContentScale.Crop
        )


        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = userProfile?.displayName ?: "Unknown User",
            style = MaterialTheme.typography.headlineSmall,
            color = MaterialTheme.colorScheme.onBackground,
            fontWeight = FontWeight.Bold
        )

        Text(
            text = userEmail ?: "Unknown Email",
            style = MaterialTheme.typography.bodyMedium,
            color = Color.Gray
        )

        Spacer(modifier = Modifier.height(24.dp))

        HorizontalDivider(thickness = 2.dp, color = MaterialTheme.colorScheme.outlineVariant)

        ProfileOptionItem(
            icon = Icons.Default.Person,
            text = "Account Info",
            onClick = { navController.navigate(AppScreen.EditProfile.name) }
        )

        HorizontalDivider(thickness = 2.dp, color = MaterialTheme.colorScheme.outlineVariant)

        ProfileOptionItem(
            icon = Icons.Default.Settings,
            text = "Settings",
            onClick = { /* TODO */ }
        )

        HorizontalDivider(thickness = 2.dp, color = MaterialTheme.colorScheme.outlineVariant)

        ProfileOptionItem(
            icon = Icons.Default.Notifications,
            text = "Notifications",
            onClick = { /* TODO */ }
        )

        HorizontalDivider(thickness = 2.dp, color = MaterialTheme.colorScheme.outlineVariant)

        ProfileOptionItem(
            icon = Icons.AutoMirrored.Filled.ExitToApp,
            text = "Log Out",
            onClick = {
                viewModel.logout(context)
                Toast.makeText(context, "Logged out", Toast.LENGTH_SHORT).show()
                onLogoutComplete()
            }
        )

        HorizontalDivider(thickness = 2.dp, color = MaterialTheme.colorScheme.outlineVariant)
    }
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