package com.example.educhat.ui.item

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.educhat.data.model.CalendarEvent
import com.example.educhat.data.model.CalendarEventType
import com.example.educhat.data.network.CalendarRepository
import com.example.educhat.ui.theme.EduChatTheme
import kotlinx.coroutines.launch
import org.threeten.bp.LocalDate
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

    var showDatePicker by remember { mutableStateOf(false) }
    val datePickerState = rememberDatePickerState()
    val showValidationError by remember { mutableStateOf(false) }

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
                .clickable { showDatePicker = true }
        )

        if (showDatePicker) {
            DatePickerDialog(
                onDismissRequest = { showDatePicker = false },
                confirmButton = {
                    Button(
                        onClick = {
                            datePickerState.selectedDateMillis?.let { millis ->
                                selectedDate = LocalDate.ofEpochDay(millis / (1000 * 60 * 60 * 24))
                            }
                            showDatePicker = false
                        }
                    ) {
                        Text("OK")
                    }
                },
                dismissButton = {
                    Button(onClick = { showDatePicker = false }) {
                        Text("Cancel")
                    }
                }
            ) {
                DatePicker(state = datePickerState)
            }
        }

        ExposedDropdownMenuBox(
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

        if (eventName.isBlank() && showValidationError) {
            Text("Event name cannot be empty", color = MaterialTheme.colorScheme.error)
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