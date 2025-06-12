package com.example.educhat.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.educhat.R

@Composable
fun ChatItem(title: String, message: String, modifier: Modifier = Modifier) {
    Card(modifier = Modifier) {
        Row(
            modifier = Modifier
                .padding(8.dp)
                .wrapContentWidth(),
        ) {
            val image = painterResource(R.drawable.educhat_icon)

            Image(
                painter = image,
                contentDescription = null,
                modifier = Modifier
                    .size(40.dp)
                    .clip(MaterialTheme.shapes.extraLarge)
            )

            Column(
                modifier = Modifier.padding(start = 8.dp),
            ) {
                Text(
                    text = title,
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.titleSmall,
                    modifier = Modifier
                        .wrapContentWidth()
                        .padding(bottom = 2.dp)
                )
                Text(
                    text = message,
                    textAlign = TextAlign.Justify,
                    modifier = Modifier.wrapContentWidth()
                )
            }

        }
    }
}
/*


@Preview(showBackground = true)
@Composable
fun ChatPreviewLight() {
    EduChatTheme {
        ChatItem(modifier = Modifier.padding(4.dp))
    }
}

@Preview
@Composable
fun ChatPreviewDark() {
    EduChatTheme(darkTheme = true) {
        ChatItem(modifier = Modifier.padding(4.dp))
    }
}

*/