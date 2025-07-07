package com.example.educhat.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import com.example.educhat.R
import com.example.educhat.data.model.Message
import com.example.educhat.ui.theme.EduChatTheme

@Composable
fun MessageItemLeft(
    message: Message,
    senderProfileImageUrl: String?,
    modifier: Modifier = Modifier
) {
    val imagePainter = rememberAsyncImagePainter(
        model = senderProfileImageUrl.takeIf { !it.isNullOrBlank() } ?: R.drawable.profile_image
    )

    Row(
        verticalAlignment = Alignment.Bottom,
        modifier = modifier
            .padding(horizontal = 8.dp, vertical = 4.dp)
            .fillMaxWidth()
    ) {
        Image(
            painter = imagePainter,
            contentDescription = "Profile picture",
            modifier = Modifier
                .size(36.dp)
                .clip(CircleShape)
                .border(1.dp, MaterialTheme.colorScheme.onBackground, CircleShape),
            contentScale = ContentScale.Crop
        )

        Spacer(modifier = Modifier.width(6.dp))

        Card(
            shape = RoundedCornerShape(
                topStart = 0.dp,
                topEnd = 16.dp,
                bottomEnd = 16.dp,
                bottomStart = 16.dp
            ),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceVariant
            ),
            modifier = Modifier
                .wrapContentWidth()
                .widthIn(max = 300.dp)
        ) {
            Text(
                text = message.content,
                modifier = Modifier.padding(10.dp),
                textAlign = TextAlign.Start,
                color = MaterialTheme.colorScheme.onSurface
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun MessageItemLeftPreviewLight() {
    EduChatTheme(darkTheme = false) {
        MessageItemLeft(
            message = Message(
                id = 1,
                content = "Hey! Just checking in.",
                group = "test",
                sender = "user1",
                created_at = "2025-07-07T12:34:56Z"
            ),
            senderProfileImageUrl = null
        )
    }
}

@Preview(showBackground = true)
@Composable
fun MessageItemLeftPreviewDark() {
    EduChatTheme(darkTheme = true) {
        MessageItemLeft(
            message = Message(
                id = 1,
                content = "Hey! Just checking in.",
                group = "test",
                sender = "user1",
                created_at = "2025-07-07T12:34:56Z"
            ),
            senderProfileImageUrl = null
        )
    }
}