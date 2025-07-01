package com.example.educhat.ui.item

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
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
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
    var selectedDate by remember { mutableStateOf(LocalDate.now()) }
    val firstDayOfMonth = selectedDate.withDayOfMonth(1)
    val daysInMonth = selectedDate.lengthOfMonth()
    val startDayOfWeek = (firstDayOfMonth.dayOfWeek.value % 7)
    val totalBoxes = ((startDayOfWeek + daysInMonth + 6) / 7) * 7

    val dayNumbers = List(totalBoxes) { index ->
        val day = index - startDayOfWeek + 1
        if (day in 1..daysInMonth) day.toString() else ""
    }

    val daysOfWeek = DayOfWeek.values().map {
        it.getDisplayName(TextStyle.SHORT, Locale.getDefault())
    }

    val events = remember { mutableStateOf<List<CalendarEvent>>(emptyList()) }

    // Nullable: selectedDay is null means 'all events of the month'
    var selectedDay by remember { mutableStateOf<LocalDate?>(null) }
    var searchQuery by remember { mutableStateOf("") }

    var selectedCategory by remember { mutableStateOf<CalendarEventType?>(null) }

    val categories = listOf<CalendarEventType?>(null) + CalendarEventType.values().toList()

    fun getCategoryLabel(type: CalendarEventType?): String {
        return type?.displayName ?: "All"
    }

    fun getCategoryColor(category: CalendarEventType?): Color {
        return when (category) {
            CalendarEventType.Meeting -> Color(0xFF4CAF50)    // green
            CalendarEventType.Exam -> Color(0xFFF44336)       // red
            CalendarEventType.Personal -> Color(0xFF2196F3)   // blue
            null -> Color.Gray                                 // fallback color
        }
    }

    LaunchedEffect(selectedDate) {
        events.value = CalendarRepository.getEventsForMonth(
            selectedDate.year,
            selectedDate.monthValue
        )
        selectedDay = null // reset selected day when month changes
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // --- Header: Month + Prev/Next buttons ---
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            val today = LocalDate.now()
            val isPrevEnabled = selectedDate.year > today.year ||
                    (selectedDate.year == today.year && selectedDate.monthValue > today.monthValue)

            TextButton(
                onClick = {
                    if (isPrevEnabled) selectedDate = selectedDate.minusMonths(1)
                },
                enabled = isPrevEnabled
            ) {
                Text("< Prev")
            }

            Text(
                text = "${selectedDate.month.getDisplayName(TextStyle.FULL, Locale.getDefault())} ${selectedDate.year}",
                fontSize = 20.sp,
                color = MaterialTheme.colorScheme.onBackground
            )

            TextButton(
                onClick = { selectedDate = selectedDate.plusMonths(1) }
            ) {
                Text("Next >")
            }
        }

        // --- Today Button centered below header ---
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            TextButton(onClick = {
                selectedDate = LocalDate.now()
                selectedDay = null
            }) {
                Text("Today")
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        // --- Days of week headers ---
        Row(modifier = Modifier.fillMaxWidth()) {
            daysOfWeek.forEach { day ->
                Text(
                    text = day.take(3),
                    modifier = Modifier
                        .weight(1f)
                        .padding(4.dp),
                    fontSize = 14.sp,
                    color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f),
                    textAlign = TextAlign.Center
                )
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        LazyVerticalGrid(
            columns = GridCells.Fixed(7),
            modifier = Modifier
                .fillMaxWidth()
                .height(100.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp),
            horizontalArrangement = Arrangement.spacedBy(4.dp),
            userScrollEnabled = false
        ) {
            itemsIndexed(dayNumbers) { index, day ->
                val isToday = day == LocalDate.now().dayOfMonth.toString() &&
                        selectedDate.month == LocalDate.now().month &&
                        selectedDate.year == LocalDate.now().year

                val dayInt = day.toIntOrNull()

                val eventCountForDay = dayInt?.let { d ->
                    events.value.count { event ->
                        val eventDate = LocalDate.parse(event.date)
                        eventDate.year == selectedDate.year &&
                                eventDate.month == selectedDate.month &&
                                eventDate.dayOfMonth == d
                    }
                } ?: 0

                val thisDay = dayInt?.let { selectedDate.withDayOfMonth(it) }
                val isSelected = thisDay == selectedDay

                Box(
                    modifier = Modifier
                        .aspectRatio(1f)
                        .clip(RoundedCornerShape(12.dp))
                        .background(
                            when {
                                isSelected -> MaterialTheme.colorScheme.secondary
                                isToday -> MaterialTheme.colorScheme.primary
                                else -> MaterialTheme.colorScheme.surface
                            }
                        )
                        // --- ONLY selectable if day is valid (not empty) ---
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
                            color = if (isToday) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurface
                        )
                        if (eventCountForDay > 0) {
                            Spacer(modifier = Modifier.height(2.dp))
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
                                    Spacer(modifier = Modifier.width(2.dp))
                                }
                            }
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        // --- Tap outside calendar to select whole month (Add a box above search for this) ---
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp)
                .clickable {
                    selectedDay = null
                },
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = if (selectedDay == null) "Viewing all events for the month" else "Tap here to view all month events",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.primary.copy(alpha = 0.7f)
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        // --- Search Bar for whole month events ---
        TextField(
            value = searchQuery,
            onValueChange = { searchQuery = it },
            placeholder = { Text("Search all events in this month...") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )

        Spacer(modifier = Modifier.height(12.dp))

        Text(
            text = if (selectedDay == null) "Events in ${selectedDate.month.getDisplayName(TextStyle.FULL, Locale.getDefault())}" else "Events on ${selectedDay.toString()}",
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier.padding(vertical = 8.dp)
        )

        // --- Event list filtered by search and selectedDay or whole month ---
        val filteredEvents = events.value.filter { event ->
            val eventDate = LocalDate.parse(event.date)
            val matchesDate = selectedDay == null || eventDate == selectedDay
            val matchesSearch = event.event.contains(searchQuery, ignoreCase = true) ||
                    event.description.contains(searchQuery, ignoreCase = true)
            val matchesCategory = (selectedCategory == null) || (event.type == selectedCategory)
            matchesDate && matchesSearch && matchesCategory
        }

        if (filteredEvents.isEmpty()) {
            Text(
                text = "No events found.",
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