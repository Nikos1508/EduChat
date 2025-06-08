package com.example.educhat

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import com.example.educhat.ui.theme.EduChatTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            EduChatTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    ConversationItem(modifier = Modifier.padding(innerPadding))
                }
            }
        }
    }
}

@Composable
fun ConversationItem(modifier: Modifier) {
    val image = painterResource(R.drawable.educhat_icon)
    Box() {
        Image(
            painter = image,
            contentDescription = null
        )
        Text("HAAAALLO")
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    EduChatTheme {
        ConversationItem(modifier = Modifier)
    }
}