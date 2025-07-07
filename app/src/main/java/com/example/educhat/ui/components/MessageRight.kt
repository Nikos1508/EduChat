package com.example.educhat.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
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
fun MessageItemRight(
    message: Message,
    senderProfileImageUrl: String?,
    modifier: Modifier = Modifier
) {
    val imagePainter = rememberAsyncImagePainter(
        model = senderProfileImageUrl.takeIf { !it.isNullOrBlank() } ?: R.drawable.profile_image
    )

    Row(
        verticalAlignment = Alignment.Bottom,
        horizontalArrangement = Arrangement.End,
        modifier = modifier
            .padding(horizontal = 8.dp, vertical = 4.dp)
            .fillMaxWidth()
    ) {
        Card(
            shape = RoundedCornerShape(
                topStart = 16.dp,
                topEnd = 0.dp,
                bottomEnd = 16.dp,
                bottomStart = 16.dp
            ),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.primaryContainer
            ),
            modifier = Modifier
                .wrapContentWidth()
                .widthIn(max = 300.dp)
        ) {
            Text(
                text = message.content,
                modifier = Modifier.padding(10.dp),
                textAlign = TextAlign.Start,
                color = MaterialTheme.colorScheme.onPrimaryContainer
            )
        }

        Spacer(modifier = Modifier.width(6.dp))

        Image(
            painter = imagePainter,
            contentDescription = "Your profile picture",
            modifier = Modifier
                .size(36.dp)
                .clip(CircleShape)
                .border(1.dp, MaterialTheme.colorScheme.onBackground, CircleShape),
            contentScale = ContentScale.Crop
        )
    }
}

@Preview(showBackground = true)
@Composable
fun MessageItemRightPreviewLight() {
    EduChatTheme(darkTheme = false) {
        MessageItemRight(
            message = Message(
                id = 2,
                content = "All good here! Thanks for checking.",
                group = "test",
                sender = "me",
                created_at = "2025-07-07T12:35:10Z"
            ),
            senderProfileImageUrl = null
        )
    }
}

@Preview(showBackground = true)
@Composable
fun MessageItemRightPreviewDark() {
    EduChatTheme(darkTheme = true) {
        MessageItemRight(
            message = Message(
                id = 2,
                content = "All good here! Thanks for checking.",
                group = "test",
                sender = "me",
                created_at = "2025-07-07T12:35:10Z"
            ),
            senderProfileImageUrl = null
        )
    }
}