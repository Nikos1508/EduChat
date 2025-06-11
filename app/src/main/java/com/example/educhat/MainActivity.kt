package com.example.educhat

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
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

    Card(modifier = modifier) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            val image = painterResource(R.drawable.educhat_icon)

            Image(
                painter = image,
                modifier = Modifier
                    .height(40.dp)
                    .padding(4.dp),
                contentDescription = null
            )
            Text("HAAAALLO")
        }
    }
}

@Preview(showBackground = true)
@Composable
fun EduChatAppPreviewLight() {
    EduChatTheme {
        ConversationItem(modifier = Modifier.padding(4.dp))
    }
}

@Preview
@Composable
fun EduChatAppPreviewDark() {
    EduChatTheme(darkTheme = true) {
        ConversationItem(modifier = Modifier.padding(4.dp))
    }
}