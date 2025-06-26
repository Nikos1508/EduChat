package com.example.educhat.ui.item

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
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
            .padding(12.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        items(classNames) { className ->
            ExpandableTimetableCard(className)
        }
    }
}

@Composable
fun ExpandableTimetableCard(className: String) {
    val colorScheme = MaterialTheme.colorScheme
    var expanded by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { expanded = !expanded },
        colors = CardDefaults.cardColors(containerColor = colorScheme.surfaceVariant),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        shape = MaterialTheme.shapes.medium
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { expanded = !expanded }
            ) {
                Text(
                    text = className,
                    fontSize = 20.sp,
                    color = colorScheme.primary,
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.align(Alignment.Center)
                )
                Icon(
                    imageVector = if (expanded) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                    contentDescription = "Expand Arrow",
                    tint = colorScheme.primary,
                    modifier = Modifier
                        .align(Alignment.CenterEnd)
                        .size(28.dp)
                )
            }

            AnimatedVisibility(
                visible = expanded,
                enter = expandVertically(),
                exit = shrinkVertically()
            ) {
                Column(modifier = Modifier.padding(top = 12.dp)) {
                    TimetableGrid()
                }
            }
        }
    }
}

@Composable
fun TimetableGrid() {
    val colorScheme = MaterialTheme.colorScheme

    val days = listOf("Mon", "Tue", "Wed", "Thu", "Fri")
    val periods = List(7) { "${it + 1}st hour" }.toMutableList().apply {
        this[0] = "1st hour"
        this[1] = "2nd hour"
        this[2] = "3rd hour"
        this[3] = "4th hour"
        this[4] = "5th hour"
        this[5] = "6th hour"
        this[6] = "7th hour"
    }

    val dummySubjects = listOf("Math", "Bio", "CS", "Eng", "Phys", "Chem", "Hist", "Geo", "PE", "Art")

    Row(
        modifier = Modifier
            .clip(MaterialTheme.shapes.medium)
            .background(colorScheme.primaryContainer)
    ) {
        Box(
            modifier = Modifier
                .width(80.dp)
                .border(1.dp, colorScheme.outline)
                .padding(vertical = 8.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(text = "", color = colorScheme.onPrimaryContainer)
        }
        days.forEach { day ->
            Box(
                modifier = Modifier
                    .weight(1f)
                    .border(1.dp, colorScheme.outline)
                    .padding(vertical = 8.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(text = day, color = colorScheme.onPrimaryContainer)
            }
        }
    }

    periods.forEachIndexed { rowIndex, periodLabel ->
        Row(
            modifier = Modifier
                .clip(MaterialTheme.shapes.small)
                .background(colorScheme.surface)
        ) {
            Box(
                modifier = Modifier
                    .width(80.dp)
                    .height(44.dp)
                    .border(1.dp, colorScheme.outlineVariant),
                contentAlignment = Alignment.Center
            ) {
                Text(text = periodLabel, fontSize = 13.sp, color = colorScheme.onSurface)
            }

            repeat(5) { colIndex ->
                val subject = dummySubjects[(rowIndex + colIndex) % dummySubjects.size]
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .height(44.dp)
                        .border(1.dp, colorScheme.outlineVariant),
                    contentAlignment = Alignment.Center
                ) {
                    Text(text = subject, fontSize = 13.sp, color = colorScheme.onSurface)
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