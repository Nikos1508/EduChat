package com.example.educhat.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
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
    senderName: String = "Οδυσσέας",
    timestamp: String = "10:18",
    modifier: Modifier = Modifier
) {
    val imagePainter = rememberAsyncImagePainter(
        model = senderProfileImageUrl.takeIf { !it.isNullOrBlank() } ?: R.drawable.profile_image
    )

    Row(
        verticalAlignment = Alignment.Top,
        horizontalArrangement = Arrangement.End,
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

        Column {
            Text(
                text = senderName,
                color = MaterialTheme.colorScheme.primary,
                style = MaterialTheme.typography.labelMedium,
                modifier = Modifier.padding(start = 4.dp, bottom = 2.dp)
            )

            Card(
                shape = RoundedCornerShape(
                    topStart = 0.dp,
                    topEnd = 16.dp,
                    bottomEnd = 16.dp,
                    bottomStart = 16.dp
                ),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.onBackground
                ),
                modifier = Modifier
                    .wrapContentWidth()
                    .widthIn(max = 300.dp)
            ) {
                Column(modifier = Modifier.padding(10.dp)) {
                    Text(
                        text = message.content,
                        color = MaterialTheme.colorScheme.surface,
                        style = MaterialTheme.typography.bodyMedium
                    )

                    Text(
                        text = timestamp,
                        color = MaterialTheme.colorScheme.surface.copy(alpha = 0.6f),
                        style = MaterialTheme.typography.labelSmall,
                        modifier = Modifier
                            .align(Alignment.End)
                            .padding(top = 4.dp)
                    )
                }
            }
        }
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