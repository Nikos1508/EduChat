package com.example.educhat.ui.item

import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.educhat.ui.components.Keyboard
import com.example.educhat.ui.theme.EduChatTheme

@Composable
fun ChatScreen(modifier: Modifier = Modifier) {
    Keyboard()
}

@Preview(showBackground = true)
@Composable
fun MessagePreviewLight() {
    EduChatTheme {
        ChatScreen(modifier = Modifier.padding(4.dp))
    }
}

@Preview
@Composable
fun MessagePreviewDark() {
    EduChatTheme(darkTheme = true) {
        ChatScreen(modifier = Modifier.padding(4.dp))
    }
}