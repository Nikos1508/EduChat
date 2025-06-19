package com.example.educhat.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.educhat.HomeScreen
import com.example.educhat.R
import com.example.educhat.ui.theme.EduChatTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBar(
    currentScreen: HomeScreen,
    onNavigate: (HomeScreen) -> Unit,
    onBack: () -> Unit,
    ChatName: String? = null
) {
    val profileImage = painterResource(R.drawable.profile_image)

    TopAppBar(
        title = {
            when (currentScreen) {
                HomeScreen.Home -> Text("EduChat")
                HomeScreen.Chat -> Text(ChatName ?: "Group Chat")
                HomeScreen.Profile -> Text("EduChat")
            }
        },
        navigationIcon = {
            when (currentScreen) {
                HomeScreen.Profile, HomeScreen.Chat -> {
                    IconButton(onClick = onBack) {  // just call onBack here
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
                else -> {}
            }
        },
        actions = {
            when (currentScreen) {
                HomeScreen.Home, HomeScreen.Chat -> {
                    IconButton(onClick = { onNavigate(HomeScreen.Profile) }) {
                        Image(
                            painter = profileImage,
                            contentDescription = "Profile",
                            modifier = Modifier
                                .size(40.dp)
                                .padding(2.dp)
                                .clip(MaterialTheme.shapes.extraLarge)
                        )
                    }
                }
                else -> {}
            }
        },
        modifier = Modifier.padding(2.dp)
    )
}

@Preview(showBackground = true)
@Composable
fun TopBarPreviewHome() {
    EduChatTheme {
        TopBar(
            currentScreen = HomeScreen.Home,
            onNavigate = {},
            onBack = {}  // add this
        )
    }
}

@Preview(showBackground = true)
@Composable
fun TopBarPreviewChat() {
    EduChatTheme {
        TopBar(
            currentScreen = HomeScreen.Chat,
            onNavigate = {},
            onBack = {},  // add this
            ChatName = "Math Study Group"
        )
    }
}

@Preview(showBackground = true)
@Composable
fun TopBarPreviewProfile() {
    EduChatTheme {
        TopBar(
            currentScreen = HomeScreen.Profile,
            onNavigate = {},
            onBack = {}  // add this
        )
    }
}