package com.example.educhat.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.educhat.ui.theme.EduChatTheme

@Composable
fun Keyboard(modifier: Modifier = Modifier) {
    var message by remember { mutableStateOf("") }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(8.dp),
        verticalArrangement = Arrangement.Bottom
    ) {
        TextField(
            value = message,
            onValueChange = { newValue -> message = newValue },
            modifier = Modifier
                .fillMaxWidth()
                .clip(MaterialTheme.shapes.large),
            placeholder = { Text("Write your message...") }
        )
    }
}

@Preview(showBackground = true)
@Composable
fun KeyboardPreviewLight() {
    EduChatTheme {
        Keyboard(modifier = Modifier)
    }
}

@Preview
@Composable
fun KeyboardPreviewDark() {
    EduChatTheme(darkTheme = true) {
        Keyboard(modifier = Modifier)
    }
}