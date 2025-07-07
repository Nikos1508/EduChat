package com.example.educhat.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import com.example.educhat.AppScreen
import com.example.educhat.R
import com.example.educhat.ui.theme.EduChatTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBar(
    currentScreen: AppScreen,
    onNavigate: (AppScreen) -> Unit,
    onBack: () -> Unit,
    onSave: (() -> Unit)? = null,
    ChatName: String? = null,
    profileImageUrl: String? = null
) {
    val profilePainter = rememberAsyncImagePainter(
        model = ImageRequest.Builder(LocalContext.current)
            .data(profileImageUrl ?: R.drawable.profile_image)
            .crossfade(true)
            .error(R.drawable.profile_image)
            .build()
    )

    TopAppBar(
        title = {
            when (currentScreen) {
                AppScreen.Home -> Text(stringResource(R.string.app_name))
                AppScreen.Chat -> Text(ChatName ?: stringResource(R.string.group_chat))
                AppScreen.Profile -> Text(stringResource(R.string.your_profile))
                AppScreen.Program -> Text(stringResource(R.string.programs))
                AppScreen.ProgramEdit -> Text(stringResource(R.string.edit_program))
                AppScreen.Calendar -> Text(stringResource(R.string.calendar))
                AppScreen.CalendarEdit -> Text(stringResource(R.string.edit_calendar))
                AppScreen.Login -> {}
                AppScreen.SignUp -> {}
                AppScreen.EditProfile -> Text(stringResource(R.string.edit_profile))
            }
        },
        navigationIcon = {
            when (currentScreen) {
                AppScreen.Profile, AppScreen.Chat, AppScreen.Program, AppScreen.ProgramEdit,
                AppScreen.Calendar, AppScreen.CalendarEdit, AppScreen.EditProfile -> {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = stringResource(R.string.back))
                    }
                }
                AppScreen.Home, AppScreen.Login, AppScreen.SignUp -> {}
            }
        },
        actions = {
            if (onSave != null) {
                TextButton(onClick = onSave) {
                    Text(
                        stringResource(R.string.save),
                        fontSize = 18.sp
                    )
                }
            } else {
                when (currentScreen) {
                    AppScreen.Home, AppScreen.Calendar, AppScreen.Program, AppScreen.Chat, AppScreen.Profile -> {

                        if (currentScreen in listOf(AppScreen.Home, AppScreen.Profile, AppScreen.Program, AppScreen.Chat)) {
                            IconButton(onClick = { onNavigate(AppScreen.Calendar) }) {
                                Icon(
                                    imageVector = Icons.Filled.CalendarToday,
                                    contentDescription = stringResource(R.string.calendar)
                                )
                            }
                        }

                        if (currentScreen in listOf(AppScreen.Home, AppScreen.Calendar, AppScreen.Program, AppScreen.Chat, AppScreen.Profile)) {
                            IconButton(onClick = { onNavigate(AppScreen.Program) }) {
                                Icon(
                                    imageVector = Icons.AutoMirrored.Filled.List,
                                    contentDescription = stringResource(R.string.programs)
                                )
                            }
                        }

                        val excludedProfileScreens = listOf(
                            AppScreen.Login,
                            AppScreen.SignUp,
                            AppScreen.Profile,
                            AppScreen.EditProfile
                        )

                        if (currentScreen !in excludedProfileScreens) {
                            IconButton(onClick = { onNavigate(AppScreen.Profile) }) {
                                Image(
                                    painter = profilePainter,
                                    contentDescription = stringResource(R.string.profile),
                                    modifier = Modifier
                                        .size(40.dp)
                                        .padding(2.dp)
                                        .clip(CircleShape)
                                )
                            }
                        }
                    }
                    AppScreen.CalendarEdit, AppScreen.ProgramEdit -> {
                        IconButton(onClick = { onNavigate(AppScreen.Profile) }) {
                            Image(
                                painter = profilePainter,
                                contentDescription = stringResource(R.string.profile),
                                modifier = Modifier
                                    .size(40.dp)
                                    .padding(2.dp)
                                    .clip(CircleShape)
                            )
                        }
                    }
                    AppScreen.Profile, AppScreen.Login, AppScreen.SignUp -> {
                    }
                    else -> {
                        val excludedProfileScreens = listOf(
                            AppScreen.Login,
                            AppScreen.SignUp,
                            AppScreen.Profile,
                            AppScreen.EditProfile
                        )
                        if (currentScreen !in excludedProfileScreens) {
                            IconButton(onClick = { onNavigate(AppScreen.Profile) }) {
                                Image(
                                    painter = profilePainter,
                                    contentDescription = stringResource(R.string.profile),
                                    modifier = Modifier
                                        .size(40.dp)
                                        .padding(2.dp)
                                        .clip(CircleShape)
                                )
                            }
                        }
                    }
                }
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

@Preview(showBackground = true)
@Composable
fun TopBarPreviewEditProfile() {
    EduChatTheme {
        TopBar(
            currentScreen = AppScreen.EditProfile,
            onNavigate = {},
            onBack = {},
            onSave = {}
        )
    }
}