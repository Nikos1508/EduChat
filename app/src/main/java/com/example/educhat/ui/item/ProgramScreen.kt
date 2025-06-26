package com.example.educhat.ui.item

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.educhat.ui.theme.EduChatTheme

@Composable
fun ProgramScreen() {
    val classNames = List(15) { "Class ${it + 1}" }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(8.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        items(classNames) { className ->
            TimetableCard(className)
        }
    }
}

@Composable
fun TimetableCard(className: String) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(4.dp, shape = MaterialTheme.shapes.medium),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFE8F0FE))
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Text(
                text = className,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.align(Alignment.CenterHorizontally),
                color = Color(0xFF1A73E8)
            )

            Spacer(modifier = Modifier.height(8.dp))

            Row(modifier = Modifier.fillMaxWidth()) {
                listOf("Mon", "Tue", "Wed", "Thu", "Fri").forEach { day ->
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .border(1.dp, Color.Gray)
                            .background(Color(0xFFD2E3FC))
                            .padding(6.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(text = day, fontWeight = FontWeight.Medium)
                    }
                }
            }

            val dummySubjects = listOf(
                "Math", "Bio", "CS", "Eng", "Phys", "Chem",
                "Hist", "Geo", "PE", "Art"
            )

            repeat(7) { row ->
                Row(modifier = Modifier.fillMaxWidth()) {
                    repeat(5) { col ->
                        val subject = dummySubjects[(row + col) % dummySubjects.size]
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .height(40.dp)
                                .border(1.dp, Color.LightGray)
                                .background(Color.White),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(text = subject, fontSize = 12.sp)
                        }
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ProgramScreenPreviewLight() {
    EduChatTheme(darkTheme = false) {
        ProgramScreen()
    }
}

@Preview(showBackground = true)
@Composable
fun ProgramScreenPreviewDark() {
    EduChatTheme(darkTheme = true) {
        ProgramScreen()
    }
}