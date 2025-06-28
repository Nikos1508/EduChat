package com.example.educhat.ui.item

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
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.educhat.ProgramViewModel
import com.example.educhat.data.model.Program
import com.example.educhat.ui.theme.EduChatTheme

@Composable
fun ProgramEditScreen(viewModel: ProgramViewModel = viewModel()) {
    val programs by viewModel.programs.collectAsState()
    var filterText by remember { mutableStateOf(TextFieldValue("")) }

    var grade by remember { mutableIntStateOf(1) }
    var classNum by remember { mutableIntStateOf(1) }
    var hour by remember { mutableIntStateOf(1) }
    var monday by remember { mutableStateOf("") }
    var tuesday by remember { mutableStateOf("") }
    var wednesday by remember { mutableStateOf("") }
    var thursday by remember { mutableStateOf("") }
    var friday by remember { mutableStateOf("") }

    fun clearInputs() {
        monday = ""; tuesday = ""; wednesday = ""; thursday = ""; friday = ""
    }

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Text("Add New Program", style = MaterialTheme.typography.titleMedium)

        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            DropdownNumberPicker("Grade", grade, 1..3) { grade = it }
            DropdownNumberPicker("Class", classNum, 1..5) { classNum = it }
            DropdownNumberPicker("Hour", hour, 1..7) { hour = it }
        }

        Spacer(Modifier.height(8.dp))

        listOf(
            "Monday" to monday,
            "Tuesday" to tuesday,
            "Wednesday" to wednesday,
            "Thursday" to thursday,
            "Friday" to friday
        ).forEach { (day, value) ->
            OutlinedTextField(
                value = value,
                onValueChange = {
                    when (day) {
                        "Monday" -> monday = it
                        "Tuesday" -> tuesday = it
                        "Wednesday" -> wednesday = it
                        "Thursday" -> thursday = it
                        "Friday" -> friday = it
                    }
                },
                label = { Text(day) },
                modifier = Modifier.fillMaxWidth().padding(vertical = 2.dp)
            )
        }

        Spacer(Modifier.height(8.dp))

        Button(
            onClick = {
                viewModel.addProgram(
                    Program(
                        id = 0,
                        grade = grade,
                        `class` = classNum,
                        hour = hour,
                        monday = monday,
                        tuesday = tuesday,
                        wednesday = wednesday,
                        thursday = thursday,
                        friday = friday
                    )
                )
                clearInputs()
            },
            modifier = Modifier.align(Alignment.End)
        ) {
            Text("Add")
        }

        Spacer(Modifier.height(16.dp))

        OutlinedTextField(
            value = filterText,
            onValueChange = { filterText = it },
            label = { Text("Filter by subject") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(Modifier.height(8.dp))

        LazyColumn(modifier = Modifier.weight(1f)) {
            items(programs.filter {
                filterText.text.isBlank() || listOf(
                    it.monday, it.tuesday, it.wednesday, it.thursday, it.friday
                ).any { subject -> subject.contains(filterText.text, ignoreCase = true) }
            }) { program ->
                Text(
                    text = "Grade ${program.grade} - Class ${program.`class`} | Hour ${program.hour}: ${program.monday}, ${program.tuesday}, ${program.wednesday}, ${program.thursday}, ${program.friday}",
                    modifier = Modifier.padding(vertical = 4.dp)
                )
            }
        }
    }
}

@Composable
fun DropdownNumberPicker(
    label: String,
    selected: Int,
    range: IntRange,
    onValueChange: (Int) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    Box {
        OutlinedButton(onClick = { expanded = true }) {
            Text("$label: $selected")
        }

        DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
            range.forEach { number ->  // use explicit name here instead of 'it'
                DropdownMenuItem(
                    text = { Text(number.toString()) },
                    onClick = {
                        onValueChange(number)
                        expanded = false
                    }
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ProgramEditScreenLightPreview() {
    EduChatTheme(darkTheme = false) {
        ProgramEditScreen()
    }
}

@Preview(showBackground = true)
@Composable
fun ProgramEditScreenDarkPreview() {
    EduChatTheme(darkTheme = true) {
        ProgramEditScreen()
    }
}