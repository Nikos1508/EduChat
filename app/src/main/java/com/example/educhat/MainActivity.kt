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
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.educhat.ui.components.TopBar
import com.example.educhat.ui.item.ChatScreen
import com.example.educhat.ui.item.HomeScreen
import com.example.educhat.ui.item.LoginScreen
import com.example.educhat.ui.item.ProfileScreen
import com.example.educhat.ui.item.SignUpScreen
import com.example.educhat.ui.theme.EduChatTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

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
    Chat
}

@Composable
fun EduChatApp() {
    val navController = rememberNavController()
    val viewModel: SupabaseAuthViewModel = viewModel() // Assuming this ViewModel exists and is set up
    val context = LocalContext.current // Keep if needed by ViewModel

    // Get current back stack entry
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    // Determine current screen based on the route
    val currentRoute = navBackStackEntry?.destination?.route

    // Convert currentRoute (String?) to AppScreen enum
    val currentScreen = remember(currentRoute) {
        currentRoute?.let { routeName ->
            try {
                AppScreen.valueOf(routeName)
            } catch (e: IllegalArgumentException) {
                null // Or a default screen, or handle error
            }
        }
    }


    LaunchedEffect(Unit) { // This seems fine for one-time effects like checking login
        viewModel.isUserLoggedIn(context)
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            if (currentScreen != null && currentScreen != AppScreen.Login) {
                TopBar(
                    currentScreen = currentScreen,
                    onNavigate = { screen -> navController.navigate(screen.name) },
                    onBack = { navController.popBackStack() }
                    // ChatName = if (currentScreen == AppScreen.Chat) "Some Chat Name" else null // Optional
                )
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = AppScreen.Login.name,
            modifier = Modifier.padding(innerPadding) // Apply padding from Scaffold
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
                    viewModel = viewModel, // pass your shared ViewModel if needed
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
                        .fillMaxSize() // Already applied by Scaffold's content area
                        .padding(8.dp) // This is fine for additional content padding
                )
            }
            // Modified Chat route to accept an optional argument (example)
            composable(AppScreen.Chat.name + "/{groupName}") { backStackEntry ->
                val groupName = backStackEntry.arguments?.getString("groupName")
                ChatScreen(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(8.dp)
                )
            }
            // Fallback Chat route if no groupName is provided (optional)
            composable(AppScreen.Chat.name) {
                ChatScreen(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(8.dp)
                )
            }
        }
    }
}