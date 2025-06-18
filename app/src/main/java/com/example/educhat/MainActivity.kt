package com.example.educhat

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.educhat.ui.components.LoadingComponent
import com.example.educhat.ui.components.TopBar
import com.example.educhat.ui.item.ChatScreen
import com.example.educhat.ui.item.HomeScreen
import com.example.educhat.ui.item.ProfileScreen
import com.example.educhat.ui.model.UserState
import com.example.educhat.ui.theme.EduChatTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            EduChatTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    MainScreen(modifier = Modifier.padding(innerPadding))
                }
            }
        }
    }
}

@Composable
fun MainScreen(
    viewModel: SupabaseAuthViewModel = viewModel(),
    modifier: Modifier
) {
    val context = LocalContext.current
    val userState by viewModel.userState

    var userEmail by remember { mutableStateOf("") }
    var userPassword by remember { mutableStateOf("") }

    var currentUserState by remember { mutableStateOf("") }

    LaunchedEffect(Unit) {
        viewModel.isUserLoggedIn(
            context,
        )
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(8.dp)
    ) {
        TextField(
            value = userEmail,
            placeholder = {
                Text(text = "Enter email")
            },
            onValueChange = {
                userEmail = it
            })

        Spacer(modifier = Modifier.padding(8.dp))

        TextField(
            value = userPassword,
            placeholder = {
                Text(text = "Enter password")
            },
            onValueChange = {
                userPassword = it
            }
        )

        Spacer(modifier = Modifier.padding(8.dp))

        Button(onClick = {
            viewModel.signUp(
                context,
                userEmail,
                userPassword,
            )
        }) {
            Text(text = "Sign Up")
        }

        Button(onClick = {
            viewModel.login(
                context,
                userEmail,
                userPassword,
            )
        }) {
            Text(text = "Login")
        }

        Button(
            colors = ButtonDefaults.buttonColors(containerColor = Color.Red),
            onClick = {
                viewModel.logout(context)
            }) {
            Text(text = "Logout")
        }

        when (userState) {
            is UserState.Loading -> {
                LoadingComponent()
            }

            is UserState.Success -> {
                val message = (userState as UserState.Success).message
                currentUserState = message
            }

            is UserState.Error -> {
                val message = (userState as UserState.Error).message
                currentUserState = message
            }
        }

        if (currentUserState.isNotEmpty()) {
            Text(text = currentUserState)
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
        MainScreen(modifier = Modifier.padding(4.dp))
    }
}

@Preview(showBackground = true)
@Composable
fun EduChatAppPreviewDark() {
    EduChatTheme (darkTheme = true) {
        MainScreen(modifier = Modifier.padding(4.dp))
    }
}