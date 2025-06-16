package com.example.educhat

import android.os.Build
import android.os.Bundle
import android.view.WindowInsets
import android.view.WindowInsetsController
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.view.WindowCompat
import com.example.educhat.ui.item.ChatScreen
import com.example.educhat.ui.theme.EduChatTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            WindowCompat.setDecorFitsSystemWindows(window, false)

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                WindowCompat.setDecorFitsSystemWindows(window, false)

                window.insetsController?.apply {
                    hide(WindowInsets.Type.navigationBars())
                    systemBarsBehavior = WindowInsetsController.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
                }
            }

            EduChatTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    ChatScreen( modifier = Modifier.padding(innerPadding) )
                }
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