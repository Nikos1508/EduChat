package com.example.educhat

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.educhat.ui.components.TopBar
import com.example.educhat.ui.item.ChatScreen
import com.example.educhat.ui.item.HomeScreen
import com.example.educhat.ui.item.ProfileScreen
import com.example.educhat.ui.theme.EduChatTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            EduChatTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    EduChatApp(modifier = Modifier.padding(innerPadding))
                }
            }
        }
    }
}

enum class HomeScreen {
    Home,
    Profile,
    Chat
}

@Composable
fun EduChatApp(
    modifier: Modifier,
    navController: NavHostController = rememberNavController()
) {
    Scaffold(
        topBar = {
            TopBar(
                onNavigate = { screen ->
                    when (screen) {
                        HomeScreen.Home -> navController.navigate("home")
                        HomeScreen.Profile -> navController.navigate("profile")
                        HomeScreen.Chat -> navController.navigate("chat")
                    }
                }
            )
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = HomeScreen.Home.name,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(route = HomeScreen.Home.name) {
                HomeScreen(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(8.dp)
                )
            }
            composable(route = HomeScreen.Profile.name) {
                ProfileScreen(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(8.dp)
                )
            }
            composable(route = HomeScreen.Chat.name) {
                ChatScreen(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(8.dp)
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun EduChatAppPreviewLight() {
    EduChatTheme {
        ChatScreen(modifier = Modifier)
    }
}

@Preview(showBackground = true)
@Composable
fun EduChatAppPreviewDark() {
    EduChatTheme (darkTheme = true) {
        ChatScreen(modifier = Modifier)
    }
}