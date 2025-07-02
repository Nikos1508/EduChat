package com.example.educhat.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.educhat.R
import com.example.educhat.ui.theme.EduChatTheme

@Composable
fun MessageItemRight(text: String, modifier: Modifier = Modifier) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp, vertical = 4.dp),
        horizontalArrangement = Arrangement.End
    ) {
        Column(horizontalAlignment = Alignment.End) {
            Card(
                shape = RoundedCornerShape(
                    topStart = 18.dp,
                    topEnd = 18.dp,
                    bottomEnd = 2.dp,
                    bottomStart = 18.dp
                ),
                modifier = Modifier
                    .padding(end = 44.dp)
            ) {
                Row(
                    modifier = Modifier
                        .padding(8.dp)
                        .wrapContentWidth()
                        .widthIn(max = 400.dp)
                ) {
                    Text(
                        text = text,
                        textAlign = TextAlign.Start,
                        modifier = Modifier.wrapContentWidth()
                    )
                }
            }

            val image = painterResource(R.drawable.educhat_icon)

            Image(
                painter = image,
                contentDescription = null,
                modifier = Modifier
                    .size(40.dp)
                    .clip(MaterialTheme.shapes.extraLarge)
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun MessageItemRightPreviewLight() {
    EduChatTheme(darkTheme = false) {
        MessageItemRight(text = "Test Light", modifier = Modifier.padding(4.dp))
    }
}

@Preview
@Composable
fun MessageItemRightPreviewDark() {
    EduChatTheme(darkTheme = true) {
        MessageItemRight(text = "Test Dark", modifier = Modifier.padding(4.dp))
    }
}