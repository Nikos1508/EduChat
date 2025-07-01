package com.example.educhat.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.CalendarToday
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
                AppScreen.Profile -> Text("Your Profile")
                AppScreen.Program -> Text("Programs")
                AppScreen.ProgramEdit -> Text("Edit Program")
                AppScreen.Calendar -> Text("Calendar")
                AppScreen.CalendarEdit -> Text("Edit Calendar")
                AppScreen.Login -> {}
                AppScreen.SignUp -> {}
            }
        },
        navigationIcon = {
            when (currentScreen) {
                AppScreen.Profile, AppScreen.Chat, AppScreen.Program,
                AppScreen.ProgramEdit, AppScreen.Calendar, AppScreen.CalendarEdit -> {

                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
                AppScreen.Home, AppScreen.Login, AppScreen.SignUp -> {}
            }
        },
        actions = {
            when (currentScreen) {
                AppScreen.Home, AppScreen.Chat, AppScreen.Program,
                AppScreen.ProgramEdit, AppScreen.Calendar, AppScreen.CalendarEdit -> {

                    val excludedScreens = listOf(AppScreen.Program, AppScreen.ProgramEdit)
                    if (currentScreen !in excludedScreens) {
                        IconButton(onClick = { onNavigate(AppScreen.Program) }) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.List,
                                contentDescription = "Programs"
                            )
                        }
                    }

                    if (currentScreen == AppScreen.Program) {
                        IconButton(onClick = { onNavigate(AppScreen.ProgramEdit) }) {
                            Icon(
                                imageVector = Icons.Filled.AddCircle,
                                contentDescription = "Edit Program"
                            )
                        }
                    }

                    if (currentScreen == AppScreen.Calendar) {
                        IconButton(onClick = { onNavigate(AppScreen.CalendarEdit) }) {
                            Icon(
                                imageVector = Icons.Filled.AddCircle,
                                contentDescription = "Add Calendar"
                            )
                        }
                    } else {
                        IconButton(onClick = { onNavigate(AppScreen.Calendar) }) {
                            Icon(
                                imageVector = Icons.Default.CalendarToday,
                                contentDescription = "Calendar"
                            )
                        }
                    }

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
                }
                AppScreen.Profile, AppScreen.Login, AppScreen.SignUp -> {}
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

@Preview(showBackground = true)
@Composable
fun TopBarPreviewProgramEdit() {
    EduChatTheme {
        TopBar(
            currentScreen = AppScreen.ProgramEdit,
            onNavigate = {},
            onBack = {}
        )
    }
}

@Preview(showBackground = true)
@Composable
fun TopBarPreviewCalendar() {
    EduChatTheme {
        TopBar(
            currentScreen = AppScreen.Calendar,
            onNavigate = {},
            onBack = {}
        )
    }
}