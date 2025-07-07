package com.example.educhat.ui.item

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.educhat.data.model.CalendarEvent
import com.example.educhat.data.model.CalendarEventType
import com.example.educhat.data.network.CalendarRepository
import com.example.educhat.ui.theme.EduChatTheme
import kotlinx.coroutines.launch
import org.threeten.bp.LocalDate
import org.threeten.bp.YearMonth
import org.threeten.bp.format.DateTimeFormatter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CalendarEditScreen() {
    val isInPreview = LocalInspectionMode.current

    var selectedDate by remember {
        mutableStateOf(if (isInPreview) LocalDate.of(2025, 1, 1) else LocalDate.now())
    }

    var eventName by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var selectedType by remember { mutableStateOf<CalendarEventType>(CalendarEventType.Meeting) }
    val categories = CalendarEventType.values().toList()
    val dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")

    var expanded by remember { mutableStateOf(false) }
    var isLoading by remember { mutableStateOf(false) }
    var successMessage by remember { mutableStateOf<String?>(null) }
    val scope = rememberCoroutineScope()

    var showDatePickerBottomSheet by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(text = "Add New Calendar Event", style = MaterialTheme.typography.headlineMedium)

        OutlinedTextField(
            value = eventName,
            onValueChange = {
                if (it.length <= 50) eventName = it
            },
            label = { Text("Event Name") },
            supportingText = { Text("${eventName.length} / 50") },
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = description,
            onValueChange = {
                if (it.length <= 50) description = it
            },
            label = { Text("Description") },
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = selectedDate.format(dateFormatter),
            onValueChange = {},
            label = { Text("Date") },
            readOnly = true,
            modifier = Modifier
                .fillMaxWidth()
                .clickable { showDatePickerBottomSheet = true }
        )

        if (showDatePickerBottomSheet) {
            DatePickerBottomSheet(
                initialDate = selectedDate,
                onDismissRequest = { showDatePickerBottomSheet = false },
                onDateSelected = { newDate ->
                    selectedDate = newDate
                    showDatePickerBottomSheet = false
                }
            )
        }

        androidx.compose.material3.ExposedDropdownMenuBox(
            expanded = expanded,
            onExpandedChange = { expanded = !expanded },
            modifier = Modifier.fillMaxWidth()
        ) {
            OutlinedTextField(
                readOnly = true,
                value = selectedType.displayName,
                onValueChange = {},
                label = { Text("Category") },
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                modifier = Modifier
                    .menuAnchor()
                    .fillMaxWidth()
            )
            ExposedDropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {
                categories.forEach { category ->
                    DropdownMenuItem(
                        text = { Text(category.displayName) },
                        onClick = {
                            selectedType = category
                            expanded = false
                        }
                    )
                }
            }
        }

        if (successMessage != null) {
            Text(text = successMessage!!, color = MaterialTheme.colorScheme.primary)
        }

        Button(
            onClick = {
                isLoading = true
                scope.launch {
                    try {
                        CalendarRepository.addEvent(
                            CalendarEvent(
                                event = eventName,
                                description = description,
                                date = selectedDate.toString(),
                                type = selectedType
                            )
                        )
                        successMessage = "Event added successfully!"
                        eventName = ""
                        description = ""
                    } catch (e: Exception) {
                        successMessage = "Error: ${e.message}"
                    } finally {
                        isLoading = false
                    }
                }
            },
            modifier = Modifier.fillMaxWidth(),
            enabled = eventName.isNotBlank() && !isLoading
        ) {
            Text(if (isLoading) "Saving..." else "Add Event")
        }
    }
}
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DatePickerBottomSheet(
    initialDate: LocalDate,
    onDismissRequest: () -> Unit,
    onDateSelected: (LocalDate) -> Unit
) {
    val years = (1900..2100).toList()
    var selectedYear by remember { mutableIntStateOf(initialDate.year) }
    var selectedMonth by remember { mutableIntStateOf(initialDate.monthValue) }
    var selectedDay by remember { mutableIntStateOf(initialDate.dayOfMonth) }

    // Calculate max days in selected month/year (handles leap years)
    val daysInMonth = YearMonth.of(selectedYear, selectedMonth).lengthOfMonth()
    val days = (1..daysInMonth).toList()

    ModalBottomSheet(
        onDismissRequest = onDismissRequest,
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text("Select Date", style = MaterialTheme.typography.headlineSmall, textAlign = TextAlign.Center)

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically
            ) {
                DropdownMenuWithLabel(
                    label = "Year",
                    options = years,
                    selectedOption = selectedYear,
                    onOptionSelected = {
                        selectedYear = it
                        // Adjust day if out of range for new month/year
                        val maxDay = YearMonth.of(selectedYear, selectedMonth).lengthOfMonth()
                        if (selectedDay > maxDay) {
                            selectedDay = maxDay
                        }
                    }
                )
                DropdownMenuWithLabel(
                    label = "Month",
                    options = (1..12).toList(),
                    selectedOption = selectedMonth,
                    onOptionSelected = {
                        selectedMonth = it
                        val maxDay = YearMonth.of(selectedYear, selectedMonth).lengthOfMonth()
                        if (selectedDay > maxDay) {
                            selectedDay = maxDay
                        }
                    }
                )
                DropdownMenuWithLabel(
                    label = "Day",
                    options = days,
                    selectedOption = selectedDay,
                    onOptionSelected = { selectedDay = it }
                )
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Button(onClick = onDismissRequest) {
                    Text("Cancel")
                }
                Button(onClick = {
                    onDateSelected(LocalDate.of(selectedYear, selectedMonth, selectedDay))
                }) {
                    Text("OK")
                }
            }
        }
    }
}

@Composable
fun DropdownMenuWithLabel(
    label: String,
    options: List<Int>,
    selectedOption: Int,
    onOptionSelected: (Int) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }
    Box {
        OutlinedTextField(
            value = selectedOption.toString(),
            onValueChange = {},
            readOnly = true,
            label = { Text(label) },
            modifier = Modifier
                .width(100.dp)
                .clickable { expanded = true }
        )
        DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
            options.forEach { option ->
                DropdownMenuItem(
                    text = { Text(option.toString()) },
                    onClick = {
                        onOptionSelected(option)
                        expanded = false
                    }
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun CalendarEditScreenLightPreview() {
    EduChatTheme(darkTheme = false) {
        CalendarEditScreen()
    }
}

@Preview(showBackground = true)
@Composable
fun CalendarEditScreenDarkPreview() {
    EduChatTheme(darkTheme = true) {
        CalendarEditScreen()
    }
}