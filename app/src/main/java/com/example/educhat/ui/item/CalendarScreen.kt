package com.example.educhat.ui.item

import android.content.Intent
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.educhat.R
import com.example.educhat.data.model.CalendarEvent
import com.example.educhat.data.model.CalendarEventType
import com.example.educhat.data.network.CalendarRepository
import com.example.educhat.ui.theme.EduChatTheme
import org.threeten.bp.DayOfWeek
import org.threeten.bp.LocalDate
import org.threeten.bp.format.TextStyle
import java.util.Locale

@Composable
fun CalendarScreen() {
    val allMonthSelected = LocalDate.MIN

    var selectedDate by remember { mutableStateOf(LocalDate.now()) }
    val firstDayOfMonth = selectedDate.withDayOfMonth(1)
    val daysInMonth = selectedDate.lengthOfMonth()
    val startDayOfWeek = (firstDayOfMonth.dayOfWeek.value - 1)
    val totalBoxes = ((startDayOfWeek + daysInMonth + 6) / 7) * 7

    val dayNumbers = List(totalBoxes) { index ->
        val day = index - startDayOfWeek + 1
        if (day in 1..daysInMonth) day.toString() else ""
    }

    val daysOfWeek = DayOfWeek.values().map {
        it.getDisplayName(TextStyle.SHORT, Locale.getDefault())
    }

    val events = remember { mutableStateOf<List<CalendarEvent>>(emptyList()) }

    var selectedDay by remember { mutableStateOf(LocalDate.now()) }

    fun getCategoryColor(category: CalendarEventType?): Color = when (category) {
        CalendarEventType.Meeting -> Color(0xFF4CAF50)
        CalendarEventType.Exam -> Color(0xFFF44336)
        CalendarEventType.Personal -> Color(0xFF2196F3)
        null -> Color.Gray
    }

    LaunchedEffect(selectedDate) {
        events.value = CalendarRepository.getEventsForMonth(
            selectedDate.year,
            selectedDate.monthValue
        )
        selectedDay = selectedDate.withDayOfMonth(1).takeIf {
            it >= LocalDate.now()
        } ?: LocalDate.now()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(12.dp)
    ) {
        // Header (prev/next, title)
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            val today = LocalDate.now()
            val isPrevEnabled = selectedDate.year > today.year ||
                    (selectedDate.year == today.year && selectedDate.monthValue > today.monthValue)

            TextButton(
                onClick = { if (isPrevEnabled) selectedDate = selectedDate.minusMonths(1) },
                enabled = isPrevEnabled
            ) { Text(stringResource(R.string.previous)) }

            Text(
                text = "${selectedDate.month.getDisplayName(TextStyle.FULL, Locale.getDefault())} ${selectedDate.year}",
                fontSize = 18.sp,
                color = MaterialTheme.colorScheme.onBackground
            )

            TextButton(onClick = { selectedDate = selectedDate.plusMonths(1) }) { Text(stringResource(R.string.next)) }
        }

        Spacer(Modifier.height(8.dp))

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 4.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            TextButton(onClick = {
                selectedDate = LocalDate.now()
                selectedDay = LocalDate.now()
            }) {
                Text(stringResource(R.string.today))
            }

            TextButton(onClick = {
                selectedDay = allMonthSelected
            }) {
                Text(
                    text = if (selectedDay == allMonthSelected) stringResource(R.string.viewing_all_events_for_the_month) else stringResource(R.string.view_all_month_events),
                    color = MaterialTheme.colorScheme.primary.copy(alpha = 0.7f)
                )
            }
        }

        Spacer(Modifier.height(6.dp))

        // Days of week row
        Row(modifier = Modifier.fillMaxWidth()) {
            daysOfWeek.forEach { day ->
                Text(
                    text = day.take(3),
                    modifier = Modifier.weight(1f).padding(2.dp),
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f),
                    textAlign = TextAlign.Center
                )
            }
        }

        Spacer(Modifier.height(4.dp))

        // Calendar grid
        LazyVerticalGrid(
            columns = GridCells.Fixed(7),
            modifier = Modifier
                .fillMaxWidth()
                .height(235.dp),
            verticalArrangement = Arrangement.spacedBy(2.dp),
            horizontalArrangement = Arrangement.spacedBy(2.dp),
            userScrollEnabled = false
        ) {
            itemsIndexed(dayNumbers) { _, day ->
                val dayInt = day.toIntOrNull()
                val thisDay = dayInt?.let { selectedDate.withDayOfMonth(it) }

                val isWeekend = thisDay?.dayOfWeek == DayOfWeek.SATURDAY || thisDay?.dayOfWeek == DayOfWeek.SUNDAY

                val isToday = day == LocalDate.now().dayOfMonth.toString() &&
                        selectedDate.month == LocalDate.now().month &&
                        selectedDate.year == LocalDate.now().year

                val isSelected = thisDay == selectedDay

                val textColor = when {
                    isToday && selectedDay != allMonthSelected -> MaterialTheme.colorScheme.onPrimary
                    isSelected -> MaterialTheme.colorScheme.onSecondary
                    else -> MaterialTheme.colorScheme.onSurface
                }

                val backgroundColor = when {
                    isToday && selectedDay != allMonthSelected -> MaterialTheme.colorScheme.primary
                    isSelected -> MaterialTheme.colorScheme.secondary
                    isWeekend -> MaterialTheme.colorScheme.surfaceVariant // lighter color for weekend
                    else -> MaterialTheme.colorScheme.surface
                }

                val eventCountForDay = dayInt?.let { d ->
                    events.value.count { event ->
                        val eventDate = LocalDate.parse(event.date)
                        eventDate.year == selectedDate.year &&
                                eventDate.month == selectedDate.month &&
                                eventDate.dayOfMonth == d
                    }
                } ?: 0

                Box(
                    modifier = Modifier
                        .aspectRatio(1f)
                        .clip(RoundedCornerShape(12.dp))
                        .background(backgroundColor)
                        .clickable(enabled = day.isNotEmpty()) {
                            thisDay?.let { selectedDay = it }
                        }
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center,
                        modifier = Modifier.fillMaxSize()
                    ) {
                        Text(
                            text = day,
                            color = textColor
                        )

                        if (eventCountForDay > 0) {
                            Spacer(Modifier.height(2.dp))
                            Row(
                                horizontalArrangement = Arrangement.Center,
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                val dayEvents = dayInt?.let { d ->
                                    events.value.filter { event ->
                                        val eventDate = LocalDate.parse(event.date)
                                        eventDate.year == selectedDate.year &&
                                                eventDate.month == selectedDate.month &&
                                                eventDate.dayOfMonth == d
                                    }
                                } ?: emptyList()

                                dayEvents.take(3).forEach { event ->
                                    Box(
                                        modifier = Modifier
                                            .size(6.dp)
                                            .background(
                                                color = getCategoryColor(event.type),
                                                shape = CircleShape
                                            )
                                    )
                                    Spacer(Modifier.width(2.dp))
                                }
                            }
                        }
                    }
                }
            }
        }

        Spacer(Modifier.height(8.dp))

        val monthName = selectedDate.month.getDisplayName(TextStyle.FULL, Locale.getDefault())

        val titleText = if (selectedDay == allMonthSelected)
            stringResource(R.string.events_in_month, monthName)
        else
            stringResource(R.string.events_on_day, selectedDay)

        Text(
            text = titleText,
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier.padding(vertical = 8.dp)
        )

        val filteredEvents = if (selectedDay == allMonthSelected) {
            events.value
        } else {
            events.value.filter {
                val eventDate = LocalDate.parse(it.date)
                eventDate == selectedDay
            }
        }

        if (filteredEvents.isEmpty()) {
            Text(
                text = stringResource(R.string.no_events_found),
                modifier = Modifier.padding(8.dp),
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
            )
        } else {
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
            ) {
                items(filteredEvents) { event ->
                    EventItem(event = event)
                }
            }
        }
    }
}

@Composable
fun EventItem(event: CalendarEvent) {
    val date = LocalDate.parse(event.date)
    val formattedDate = date.format(org.threeten.bp.format.DateTimeFormatter.ofPattern("MMM d, yyyy"))
    val context = LocalContext.current

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(MaterialTheme.colorScheme.surfaceVariant)
            .padding(12.dp)
    ) {
        Text(
            text = event.event,
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = event.description,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.8f)
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = formattedDate,
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.primary
        )

        Spacer(modifier = Modifier.height(8.dp))

        val context = LocalContext.current
        val unknown = stringResource(R.string.unknown)
        val shareLabel = stringResource(R.string.share_event)

        val shareText = stringResource(
            R.string.share_event_text,
            event.event,
            event.description,
            formattedDate,
            event.type?.displayName ?: unknown
        )

        IconButton(onClick = {
            val sendIntent = Intent().apply {
                action = Intent.ACTION_SEND
                putExtra(Intent.EXTRA_TEXT, shareText)
                type = "text/plain"
            }
            val shareIntent = Intent.createChooser(sendIntent, shareLabel)
            context.startActivity(shareIntent)
        }) {
            Icon(
                imageVector = Icons.Default.Share,
                contentDescription = shareLabel
            )
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