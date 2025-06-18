package com.example.educhat.ui.components

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Chat
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.educhat.HomeScreen
import com.example.educhat.ui.theme.EduChatTheme

/*
@Composable
fun TopBar(
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.primary)
            .padding(4.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        val profile_image = painterResource(R.drawable.profile_image)
        val image = painterResource(R.drawable.educhat_icon)

        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = image,
                contentDescription = null,
                modifier = Modifier
                    .size(40.dp)
                    .padding(2.dp)
                    .clip(MaterialTheme.shapes.small)
            )

            Text(
                text = stringResource(R.string.app_name),
                textAlign = TextAlign.Start,
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.onPrimary,
                modifier = Modifier.padding(start = 8.dp)
            )
        }

        Image(
            painter = profile_image,
            contentDescription = null,
            modifier = Modifier
                .size(40.dp)
                .padding(2.dp)
                .clip(MaterialTheme.shapes.extraLarge)
        )
    }
}
 */

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBar(
    onNavigate: (HomeScreen) -> Unit
) {
    TopAppBar(
        title = { Text("EduChat") },
        actions = {
            IconButton(onClick = { onNavigate(HomeScreen.Home) }) {
                Icon(Icons.Default.Home, contentDescription = "Home")
            }
            IconButton(onClick = { onNavigate(HomeScreen.Profile) }) {
                Icon(Icons.Default.Person, contentDescription = "Profile")
            }
            IconButton(onClick = { onNavigate(HomeScreen.Chat) }) {
                Icon(Icons.Default.Chat, contentDescription = "Chat")
            }
        },
        modifier = Modifier.padding(2.dp)
    )
}


@Preview(showBackground = true)
@Composable
fun TopBarPreviewLight() {
    EduChatTheme {
        TopBar(
            onNavigate = {}
        )
    }
}