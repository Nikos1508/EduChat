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
import com.example.educhat.ui.components.Keyboard
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

@Composable
fun EduChatApp(modifier: Modifier) {
    Keyboard(modifier = modifier)
}

@Preview(showBackground = true)
@Composable
fun EduChatAppPreviewLight() {
    EduChatTheme {
        EduChatApp(modifier = Modifier)
    }
}

@Preview(showBackground = true)
@Composable
fun EduChatAppPreviewDark() {
    EduChatTheme (darkTheme = true) {
        EduChatApp(modifier = Modifier)
    }
}