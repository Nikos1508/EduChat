package com.example.educhat

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.educhat.ui.components.ChatItem
import com.example.educhat.ui.components.TopBar
import com.example.educhat.ui.data.Chats
import com.example.educhat.ui.model.Chat
import com.example.educhat.ui.theme.EduChatTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            EduChatTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    ChatApp(modifier = Modifier.padding(innerPadding))
                }
            }
        }
    }
}

@Composable
fun ChatApp(modifier: Modifier) {
    Scaffold(
        topBar = {
            TopBar(modifier = modifier)
        },
    ) { innerPadding ->
        ChatList(
            chatList = Chats().loadChats(),
            modifier = Modifier.padding((innerPadding))
        )
    }

}

@Composable
fun ChatList(chatList: List<Chat>, modifier: Modifier = Modifier) {
    LazyColumn(modifier = modifier) {
        items(chatList) { chat ->
            ChatItem(
                chat = chat,
                modifier = Modifier.padding(8.dp)
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun EduChatAppPreviewLight() {
    EduChatTheme {
        ChatApp(modifier = Modifier)
    }
}

@Preview(showBackground = true)
@Composable
fun EduChatAppPreviewDark() {
    EduChatTheme (darkTheme = true) {
        ChatApp(modifier = Modifier)
    }
}