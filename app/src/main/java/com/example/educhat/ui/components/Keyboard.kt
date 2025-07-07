package com.example.educhat.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.ime
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Keyboard(
    modifier: Modifier = Modifier
) {
    var message by remember { mutableStateOf("") }

    val imeInsets = WindowInsets.ime.asPaddingValues()

    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.background)
            .padding(8.dp)
            .padding(bottom = imeInsets.calculateBottomPadding()),
        verticalArrangement = Arrangement.Bottom
    ) {
        TextField(
            value = message,
            onValueChange = { newValue -> message = newValue },
            modifier = Modifier
                .fillMaxWidth()
                .clip(MaterialTheme.shapes.large)
                .background(MaterialTheme.colorScheme.surface),
            placeholder = { Text("Write your message...") },
            colors = TextFieldDefaults.colors(
                    focusedContainerColor = MaterialTheme.colorScheme.surface,
                    unfocusedContainerColor = MaterialTheme.colorScheme.surface,
                    disabledContainerColor = MaterialTheme.colorScheme.surface
                )
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