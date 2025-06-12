package com.example.educhat

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.educhat.ui.components.ChatItem
import com.example.educhat.ui.data.Chats
import com.example.educhat.ui.model.Chat
import com.example.educhat.ui.theme.EduChatTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            EduChatTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    ChatList(modifier = Modifier.padding(innerPadding))
                }
            }
        }
    }
}

@Composable
fun ChatList(chatList: List<Chat>, modifier: Modifier = Modifier) {
    LazyColumn(modifier = modifier) {
        items(chatList) { chat ->
            ChatItem(title = chat, modifier = modifier)
        }
    }
}

@Preview(showBackground = true)
@Composable
fun EduChatAppPreviewLight() {
    EduChatTheme {
        ChatList(chatList = Chats().loadChats(), modifier = Modifier.padding(4.dp))
    }
}

@Preview
@Composable
fun EduChatAppPreviewDark() {
    EduChatTheme(darkTheme = true) {
        ChatList(chatList = Chats().loadChats(), modifier = Modifier.padding(4.dp))
    }
}