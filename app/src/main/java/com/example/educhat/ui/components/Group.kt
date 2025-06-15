package com.example.educhat.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
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
import com.example.educhat.ui.model.Group

@Composable
fun GroupItem(group: Group, modifier: Modifier = Modifier) {
    Card(modifier = Modifier
        .fillMaxWidth()
        .padding(8.dp)) {
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
                    .size(50.dp)
                    .clip(MaterialTheme.shapes.extraLarge)
            )

            Column(
                modifier = Modifier.padding(start = 8.dp),
            ) {
                Text(
                    text = group.title,
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.titleSmall,
                    modifier = Modifier
                        .wrapContentWidth()
                        .padding(bottom = 2.dp)
                )
                Text(
                    text = group.message,
                    textAlign = TextAlign.Justify,
                    modifier = Modifier.wrapContentWidth()
                )
            }

        }
    }
}
