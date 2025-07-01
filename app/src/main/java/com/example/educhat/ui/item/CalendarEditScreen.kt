package com.example.educhat.ui.item

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.educhat.data.model.CalendarEventType
import com.example.educhat.ui.theme.EduChatTheme
import org.threeten.bp.LocalDate
import org.threeten.bp.format.DateTimeFormatter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CalendarEditScreen() {
    val isInPreview = LocalInspectionMode.current
    val selectedDate by remember {
        mutableStateOf(if (isInPreview) LocalDate.of(2025, 1, 1) else LocalDate.now())
    }

    var eventName by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }

    var selectedType by remember { mutableStateOf<CalendarEventType>(CalendarEventType.Meeting) }
    val categories = CalendarEventType.values().toList()

    val dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(text = "Add New Calendar Event", style = MaterialTheme.typography.headlineMedium)

        OutlinedTextField(
            value = eventName,
            onValueChange = { eventName = it },
            label = { Text("Event Name") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = description,
            onValueChange = { description = it },
            label = { Text("Description") },
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = selectedDate.format(dateFormatter),
            onValueChange = { },
            label = { Text("Date (YYYY-MM-DD)") },
            singleLine = true,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier.fillMaxWidth(),
            enabled = false
        )

        var expanded by remember { mutableStateOf(false) }
        ExposedDropdownMenuBox(
            expanded = expanded,
            onExpandedChange = { expanded = !expanded },
            modifier = Modifier.fillMaxWidth()
        ) {
            OutlinedTextField(
                readOnly = true,
                value = selectedType.displayName,
                onValueChange = { },
                label = { Text("Category") },
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                modifier = Modifier.menuAnchor()
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

        Button(
            onClick = { /* no-op */ },
            modifier = Modifier.fillMaxWidth(),
            enabled = false
        ) {
            Text("Add Event")
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