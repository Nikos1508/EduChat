package com.example.educhat.ui.item

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.educhat.ui.theme.EduChatTheme
import org.threeten.bp.DayOfWeek
import org.threeten.bp.LocalDate
import org.threeten.bp.format.TextStyle
import java.util.Locale

@Composable
fun CalendarScreen() {
    val today = remember { LocalDate.now() }
    val firstDayOfMonth = remember { today.withDayOfMonth(1) }
    val daysInMonth = remember { today.lengthOfMonth() }

    val startDayOfWeek = remember {
        (firstDayOfMonth.dayOfWeek.value % 7)
    }

    val totalBoxes = remember {
        ((startDayOfWeek + daysInMonth + 6) / 7) * 7
    }

    val dayNumbers = remember {
        List(totalBoxes) { index ->
            val day = index - startDayOfWeek + 1
            if (day in 1..daysInMonth) day.toString() else ""
        }
    }

    val daysOfWeek = remember {
        DayOfWeek.values().map {
            it.getDisplayName(TextStyle.SHORT, Locale.getDefault())
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            text = "${today.month.getDisplayName(TextStyle.FULL, Locale.getDefault())} ${today.year}",
            fontSize = 24.sp,
            color = MaterialTheme.colorScheme.onBackground,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        Row(modifier = Modifier.fillMaxWidth()) {
            daysOfWeek.forEach { day ->
                Text(
                    text = day.take(3),
                    modifier = Modifier
                        .weight(1f)
                        .padding(4.dp),
                    fontSize = 14.sp,
                    color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f)
                )
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        LazyVerticalGrid(
            columns = GridCells.Fixed(7),
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(4.dp),
            horizontalArrangement = Arrangement.spacedBy(4.dp),
            userScrollEnabled = false
        ) {
            itemsIndexed(dayNumbers) { index, day ->
                val isToday = day == today.dayOfMonth.toString()

                val dayOfWeekIndex = (startDayOfWeek + index) % 7

                val isWeekend = dayOfWeekIndex == 0 || dayOfWeekIndex == 6

                val context = LocalContext.current

                val eventCountForDay = 1

                Box(
                    modifier = Modifier
                        .aspectRatio(1f)
                        .background(
                            if (isToday) MaterialTheme.colorScheme.primary.copy(alpha = 0.8f)
                            else MaterialTheme.colorScheme.surface
                        )
                        .clip(RoundedCornerShape(8.dp))
                        .padding(4.dp)
                        .clickable(enabled = day.isNotEmpty()) {
                            Toast.makeText(context, "Clicked on day $day", Toast.LENGTH_SHORT).show()
                        },
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Text(
                            text = day,
                            color = when {
                                isToday -> MaterialTheme.colorScheme.onPrimary
                                isWeekend -> MaterialTheme.colorScheme.onSurfaceVariant // dimmer text for weekends
                                else -> MaterialTheme.colorScheme.onSurface
                            }
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        Row(
                            horizontalArrangement = Arrangement.Center,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            // for each event for this day, show a dot (e.g., colored circle)
                            // example: show 2 dots for 2 events:
                            repeat(eventCountForDay) {
                                Box(
                                    modifier = Modifier
                                        .size(6.dp)
                                        .background(color = MaterialTheme.colorScheme.primary, shape = CircleShape)
                                        .padding(1.dp)
                                )
                                Spacer(modifier = Modifier.width(2.dp))
                            }
                        }
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun CalendarScreenPreviewLight() {
    EduChatTheme(darkTheme = false) {
        CalendarScreen()
    }
}

@Preview(showBackground = true)
@Composable
fun CalendarScreenPreviewDark() {
    EduChatTheme(darkTheme = true) {
        CalendarScreen()
    }
}