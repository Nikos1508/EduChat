package com.example.educhat.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.List
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
import com.example.educhat.AppScreen
import com.example.educhat.R
import com.example.educhat.ui.theme.EduChatTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBar(
    currentScreen: AppScreen,
    onNavigate: (AppScreen) -> Unit,
    onBack: () -> Unit,
    ChatName: String? = null
) {
    val profileImage = painterResource(R.drawable.profile_image)

    TopAppBar(
        title = {
            when (currentScreen) {
                AppScreen.Home -> Text("EduChat")
                AppScreen.Chat -> Text(ChatName ?: "Group Chat")
                AppScreen.Profile -> Text("EduChat")
                AppScreen.Login -> { /* Empty For Now  */}
                AppScreen.SignUp -> { /* Empty For Now */ }
                AppScreen.Program -> Text("EduChat")
            }
        },
        navigationIcon = {
            when (currentScreen) {
                AppScreen.Profile, AppScreen.Chat, AppScreen.Program -> {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
                AppScreen.Home -> {}
                AppScreen.Login -> {}
                AppScreen.SignUp -> {}
            }
        },
        actions = {
            when (currentScreen) {
                AppScreen.Home, AppScreen.Chat, AppScreen.Program -> {
                    // Profile icon
                    IconButton(onClick = { onNavigate(AppScreen.Profile) }) {
                        Image(
                            painter = profileImage,
                            contentDescription = "Profile",
                            modifier = Modifier
                                .size(40.dp)
                                .padding(2.dp)
                                .clip(MaterialTheme.shapes.extraLarge)
                        )
                    }
                    // Programs icon button
                    IconButton(onClick = { onNavigate(AppScreen.Program) }) {
                        Icon(
                            imageVector = Icons.Filled.List,
                            contentDescription = "Programs"
                        )
                    }
                }
                AppScreen.Profile -> {}
                AppScreen.Login -> {}
                AppScreen.SignUp -> {}
            }
        },
        modifier = Modifier.padding(2.dp)
    )
}

@Preview(showBackground = true)
@Composable
fun TopBarPreviewLogin() {
    EduChatTheme {
        TopBar(
            currentScreen = AppScreen.Login,
            onNavigate = {},
            onBack = {}
        )
    }
}

@Preview(showBackground = true)
@Composable
fun TopBarPreviewSignUp() {
    EduChatTheme {
        TopBar(
            currentScreen = AppScreen.SignUp,
            onNavigate = {},
            onBack = {}
        )
    }
}

@Preview(showBackground = true)
@Composable
fun TopBarPreviewHome() {
    EduChatTheme {
        TopBar(
            currentScreen = AppScreen.Home,
            onNavigate = {},
            onBack = {}
        )
    }
}

@Preview(showBackground = true)
@Composable
fun TopBarPreviewChat() {
    EduChatTheme {
        TopBar(
            currentScreen = AppScreen.Chat,
            onNavigate = {},
            onBack = {},
            ChatName = "Math Study Group"
        )
    }
}

@Preview(showBackground = true)
@Composable
fun TopBarPreviewProfile() {
    EduChatTheme {
        TopBar(
            currentScreen = AppScreen.Profile,
            onNavigate = {},
            onBack = {}
        )
    }
}

@Preview(showBackground = true)
@Composable
fun TopBarPreviewProgram() {
    EduChatTheme {
        TopBar(
            currentScreen = AppScreen.Program,
            onNavigate = {},
            onBack = {}
        )
    }
}