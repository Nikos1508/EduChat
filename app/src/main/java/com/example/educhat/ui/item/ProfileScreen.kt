package com.example.educhat.ui.item

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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.educhat.R
import com.example.educhat.ui.theme.EduChatTheme

@Composable
fun ProfileScreen(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(R.drawable.profile_image),
            contentDescription = "Profile Picture",
            modifier = Modifier
                .size(100.dp)
                .clip(CircleShape)
                .border(2.dp, Color.Gray, CircleShape)
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "John Doe",
            style = MaterialTheme.typography.headlineSmall,
            color = MaterialTheme.colorScheme.onBackground,
            fontWeight = FontWeight.Bold
        )

        Text(
            text = "john.doe@example.com",
            style = MaterialTheme.typography.bodyMedium,
            color = Color.Gray
        )

        Spacer(modifier = Modifier.height(24.dp))

        HorizontalDivider(thickness = 2.dp, color = MaterialTheme.colorScheme.outlineVariant)
        ProfileOptionItem(icon = Icons.Default.Person, text = "Account Info")
        HorizontalDivider(thickness = 2.dp, color = MaterialTheme.colorScheme.outlineVariant)
        ProfileOptionItem(icon = Icons.Default.Settings, text = "Settings")
        HorizontalDivider(thickness = 2.dp, color = MaterialTheme.colorScheme.outlineVariant)
        ProfileOptionItem(icon = Icons.Default.Notifications, text = "Notifications")
        HorizontalDivider(thickness = 2.dp, color = MaterialTheme.colorScheme.outlineVariant)
        ProfileOptionItem(icon = Icons.AutoMirrored.Filled.ExitToApp, text = "Log Out")
        HorizontalDivider(thickness = 2.dp, color = MaterialTheme.colorScheme.outlineVariant)
    }
}

@Composable
fun ProfileOptionItem(icon: ImageVector, text: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { /* TO DO */ }
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

@Preview(showBackground = true)
@Composable
fun ProfileScreenPreviewLight() {
    EduChatTheme {
        ProfileScreen(modifier = Modifier.padding(4.dp))
    }
}

@Preview
@Composable
fun ProfileScreenPreviewDark() {
    EduChatTheme(darkTheme = true) {
        ProfileScreen(modifier = Modifier.padding(4.dp))
    }
}