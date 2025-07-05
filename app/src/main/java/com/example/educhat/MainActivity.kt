package com.example.educhat

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.educhat.data.model.UserState
import com.example.educhat.ui.components.TopBar
import com.example.educhat.ui.item.CalendarEditScreen
import com.example.educhat.ui.item.CalendarScreen
import com.example.educhat.ui.item.ChatScreen
import com.example.educhat.ui.item.EditProfileScreen
import com.example.educhat.ui.item.HomeScreen
import com.example.educhat.ui.item.LoginScreen
import com.example.educhat.ui.item.ProfileScreen
import com.example.educhat.ui.item.ProgramEditScreen
import com.example.educhat.ui.item.ProgramScreen
import com.example.educhat.ui.item.SignUpScreen
import com.example.educhat.ui.theme.EduChatTheme
import com.jakewharton.threetenabp.AndroidThreeTen

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        AndroidThreeTen.init(this)

        setContent {
            EduChatTheme {
                EduChatApp()
            }
        }
    }
}

enum class AppScreen {
    Login,
    SignUp,
    Home,
    Profile,
    EditProfile,
    Chat,
    Program,
    ProgramEdit,
    Calendar,
    CalendarEdit
}

@Composable
fun EduChatApp() {
    val navController = rememberNavController()
    val viewModel: SupabaseAuthViewModel = viewModel()
    val programViewModel: ProgramViewModel = viewModel()

    val context = LocalContext.current

    val userState by viewModel.userState

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    var editProfileSaveAction by remember { mutableStateOf<(() -> Unit)?>(null) }

    val currentScreen = remember(currentRoute) {
        currentRoute?.let { routeName ->
            try {
                AppScreen.valueOf(routeName)
            } catch (e: IllegalArgumentException) {
                null
            }
        }
    }


    LaunchedEffect(Unit) {
        viewModel.isUserLoggedIn(context)
    }

    LaunchedEffect(userState) {
        when (userState) {
            is UserState.Error -> {
                val message = (userState as UserState.Error).message
                if (message.contains("Session expired", ignoreCase = true)) {
                    navController.navigate(AppScreen.Login.name) {
                        popUpTo(AppScreen.Home.name) { inclusive = true }
                    }
                }
            }
            is UserState.Success -> {
                val message = (userState as UserState.Success).message
                if (message == "User already logged in!") {
                    navController.navigate(AppScreen.Home.name) {
                        popUpTo(AppScreen.Login.name) { inclusive = true }
                    }
                }
            }
            else -> {}
        }
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            if (currentScreen != null && currentScreen != AppScreen.Login) {
                TopBar(
                    currentScreen = currentScreen,
                    onNavigate = { screen -> navController.navigate(screen.name) },
                    onBack = { navController.popBackStack() },
                    onSave = if (currentScreen == AppScreen.EditProfile) editProfileSaveAction else null,
                    profileImageUrl = viewModel.userProfile.value?.profileImageUrl
                )
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = AppScreen.Login.name,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(AppScreen.Login.name) {
                LoginScreen(
                    viewModel = viewModel,
                    onLoginSuccess = {
                        navController.navigate(AppScreen.Home.name) {
                            popUpTo(AppScreen.Login.name) { inclusive = true }
                        }
                    },
                    onSignUpClick = {
                        navController.navigate(AppScreen.SignUp.name)
                    }
                )
            }
            composable(AppScreen.SignUp.name) {
                SignUpScreen(
                    viewModel = viewModel,
                    onSignUpComplete = {
                        navController.navigate(AppScreen.Login.name) {
                            popUpTo(AppScreen.SignUp.name) { inclusive = true }
                        }
                    },
                    onBackToLogin = {
                        navController.popBackStack(AppScreen.Login.name, inclusive = false)
                    }
                )
            }
            composable(AppScreen.Home.name) {
                HomeScreen(
                    modifier = Modifier.fillMaxSize(),
                    onGroupClick = {
                        navController.navigate(AppScreen.Chat.name)
                    }
                )
            }
            composable(AppScreen.Profile.name) {
                ProfileScreen(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(8.dp),
                    viewModel = viewModel,
                    navController = navController,
                    onLogoutComplete = {
                        navController.navigate(AppScreen.Login.name) {
                            popUpTo(AppScreen.Home.name) { inclusive = true }
                        }
                    }
                )
            }
            composable(AppScreen.EditProfile.name) {
                EditProfileScreen(
                    viewModel = viewModel,
                    navController = navController,
                    onSaveAvailable = { saveAction -> editProfileSaveAction = saveAction }
                )
            }
            composable(route = AppScreen.Chat.name + "/{groupName}") { backStackEntry ->
                val groupName = backStackEntry.arguments?.getString("groupName") ?: ""
                ChatScreen(
                    groupId = groupName,
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(8.dp)
                )
            }
            composable(AppScreen.Program.name) {
                ProgramScreen()
            }
            composable(AppScreen.ProgramEdit.name) {
                ProgramEditScreen(
                    navController = navController,
                    viewModel = programViewModel
                )
            }
            composable(AppScreen.Calendar.name) {
                CalendarScreen()
            }
            composable(AppScreen.CalendarEdit.name) {
                CalendarEditScreen()
            }
        }
    }
}