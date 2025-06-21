package com.example.educhat

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.educhat.ui.item.LoginScreen
import com.example.educhat.ui.theme.EduChatTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            EduChatTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerpadding ->

                    MainScreen(modifier = Modifier.padding(innerpadding))
                    /*
                    EduChatApp(modifier = Modifier.padding(innerPadding), navController = rememberNavController())
                     */
                }
            }
        }
    }
}

/*
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

*/

enum class HomeScreen {
    Login,
    Home,
    Profile,
    Chat
}

@Composable
fun MainScreen(
    viewModel: SupabaseAuthViewModel = viewModel(),
    modifier: Modifier
) {
    val context = LocalContext.current

    LaunchedEffect(Unit) {
        viewModel.isUserLoggedIn(context)
    }

    LoginScreen(viewModel = viewModel)
}

/*
@Composable
fun EduChatApp(
    modifier: Modifier,
    navController: NavHostController = rememberNavController()
) {
    val navBackStackEntry = navController.currentBackStackEntryAsState()

    val currentRoute = navBackStackEntry.value?.destination?.route ?: HomeScreen.Home.name

    val currentScreen = HomeScreen.values().find { it.name == currentRoute } ?: HomeScreen.Home

    Scaffold(
        topBar = {
            TopBar(
                currentScreen = currentScreen,
                onNavigate = { screen ->
                    if (screen != currentScreen) {
                        navController.navigate(screen.name)
                    }
                },
                onBack = {
                    navController.popBackStack()
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
                    modifier = Modifier.fillMaxSize(),
                    onGroupClick = { navController.navigate("chat") }
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
*/