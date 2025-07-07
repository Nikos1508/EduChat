package com.example.educhat.ui.item

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.educhat.ProgramViewModel
import com.example.educhat.R
import com.example.educhat.data.model.Program

@Composable
fun ProgramEditScreen(navController: NavController, viewModel: ProgramViewModel = viewModel()) {
    val programs by viewModel.programs.collectAsState()
    var filterText by remember { mutableStateOf(TextFieldValue("")) }

    var grade by remember { mutableIntStateOf(1) }
    var classNum by remember { mutableIntStateOf(1) }
    var hour by remember { mutableIntStateOf(1) }

    val days = listOf("Monday", "Tuesday", "Wednesday", "Thursday", "Friday")
    val dayInputs = remember {
        days.associateWith { mutableStateOf("") }.toMutableMap()
    }

    val isFormValid = dayInputs.values.all { it.value.isNotBlank() }

    fun clearInputs() {
        dayInputs.forEach { it.value.value = "" }
    }

    val backgroundColor = MaterialTheme.colorScheme.background

    val classOptions = listOf("1", "2", "3", "4", "5")
    val gradeOptions = listOf("A", "B", "C")
    val hourOptions = listOf("1st", "2nd", "3rd", "4th", "5th", "6th", "7th")

    var selectedClass by remember { mutableStateOf(classOptions.first()) }
    var selectedGrade by remember { mutableStateOf(gradeOptions.first()) }
    var selectedHour by remember { mutableStateOf(hourOptions.first()) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(backgroundColor)
            .padding(16.dp)
    ) {

        Text(
            stringResource(R.string.add_new_program),
            style = MaterialTheme.typography.headlineSmall,
            color = MaterialTheme.colorScheme.primary
        )

        Spacer(Modifier.height(12.dp))

        Row(
            Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            DropdownPicker(
                label = stringResource(R.string.grade),
                options = gradeOptions,
                selectedOption = selectedGrade,
                onOptionSelected = { selectedGrade = it },
                optionLabel = { it }
            )
            DropdownPicker(
                label = stringResource(R.string.class_label),
                options = classOptions,
                selectedOption = selectedClass,
                onOptionSelected = { selectedClass = it },
                optionLabel = { it }
            )
            DropdownPicker(
                label = stringResource(R.string.hour),
                options = hourOptions,
                selectedOption = selectedHour,
                onOptionSelected = { selectedHour = it },
                optionLabel = { it }
            )
        }

        Spacer(Modifier.height(12.dp))

        days.forEach { day ->
            OutlinedTextField(
                value = dayInputs[day]?.value ?: "",
                onValueChange = { dayInputs[day]?.value = it },
                label = { Text(day) },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp),
                shape = MaterialTheme.shapes.medium,
                singleLine = true
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
                        monday = dayInputs["Monday"]?.value ?: "",
                        tuesday = dayInputs["Tuesday"]?.value ?: "",
                        wednesday = dayInputs["Wednesday"]?.value ?: "",
                        thursday = dayInputs["Thursday"]?.value ?: "",
                        friday = dayInputs["Friday"]?.value ?: ""
                    )
                )
                clearInputs()
            },
            modifier = Modifier.align(Alignment.End),
            enabled = isFormValid
        ) {
            Text(stringResource(R.string.add))
        }

        Spacer(Modifier.height(16.dp))

        OutlinedTextField(
            value = filterText,
            onValueChange = { filterText = it },
            label = { Text(stringResource(R.string.filter_by_subject)) },
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 12.dp),
            singleLine = true
        )

        Text(
            text = stringResource(R.string.preview),
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        val selectedPrograms = programs.filter {
            it.grade == grade && it.`class` == classNum
        }.sortedBy { it.hour }

        if (selectedPrograms.isNotEmpty()) {
            selectedPrograms.forEach { ProgramItem(it) }
        } else {
            Text(
                text = stringResource(R.string.no_program_for_grade_class, grade, classNum),
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.padding(8.dp)
            )
        }

        Spacer(Modifier.height(12.dp))

        LazyColumn(modifier = Modifier.weight(1f)) {
            items(programs.filter {
                filterText.text.isBlank() || listOf(
                    it.monday, it.tuesday, it.wednesday, it.thursday, it.friday
                ).any { subject -> subject.contains(filterText.text, ignoreCase = true) }
            }) { program ->
                ProgramItem(program)
            }
        }
    }
}

@Composable
fun <T> DropdownPicker(
    label: String,
    options: List<T>,
    selectedOption: T,
    onOptionSelected: (T) -> Unit,
    optionLabel: @Composable (T) -> String
) {
    var expanded by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .width(120.dp)
            .padding(horizontal = 6.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.onSurface,
            modifier = Modifier.padding(bottom = 4.dp)
        )

        OutlinedButton(
            onClick = { expanded = true },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(optionLabel(selectedOption))
            Icon(
                imageVector = Icons.Default.ArrowDropDown,
                contentDescription = null
            )
        }

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            options.forEach { option ->
                DropdownMenuItem(
                    text = { Text(optionLabel(option)) },
                    onClick = {
                        onOptionSelected(option)
                        expanded = false
                    }
                )
            }
        }
    }
}

@Composable
fun ProgramItem(program: Program) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp)
    ) {
        Text(
            text = "Grade ${program.grade} - Class ${program.`class`} | Hour ${program.hour}",
            style = MaterialTheme.typography.labelLarge
        )
        Spacer(Modifier.height(4.dp))
        Row(horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier.fillMaxWidth()) {
            listOf(
                "Mon" to program.monday,
                "Tue" to program.tuesday,
                "Wed" to program.wednesday,
                "Thu" to program.thursday,
                "Fri" to program.friday
            ).forEach { (day, subj) ->
                Column(modifier = Modifier.weight(1f)) {
                    Text(day, style = MaterialTheme.typography.labelSmall)
                    Text(subj, style = MaterialTheme.typography.bodySmall)
                }
            }
        }
    }
}