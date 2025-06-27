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
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.educhat.data.model.Program
import com.example.educhat.ui.theme.EduChatTheme
import io.github.jan.supabase.postgrest.from

@Composable
fun ProgramScreen() {
    var allPrograms by remember { mutableStateOf<List<Program>>(emptyList()) }
    var isLoading by remember { mutableStateOf(true) }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    LaunchedEffect(Unit) {
        try {
            val programs = supabase.from("programs")
                .select()
                .decodeList<Program>()

            val sortedPrograms = programs.sortedBy { it.hour } // Sort by hour
            allPrograms = sortedPrograms
        } catch (e: Exception) {
            errorMessage = e.message ?: "Unknown error"
        } finally {
            isLoading = false
        }
    }

    when {
        isLoading -> {
            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        }

        errorMessage != null -> {
            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text(text = "Error: $errorMessage", color = MaterialTheme.colorScheme.error)
            }
        }

        else -> {
            Column(modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)) {
                for (grade in 1..3) {
                    for (classNum in 1..5) {
                        val filtered = allPrograms.filter {
                            it.grade == grade && it.`class` == classNum
                        }.sortedBy { it.hour }

                        if (filtered.isNotEmpty()) {
                            ProgramTable(filtered, grade, classNum)
                            Spacer(modifier = Modifier.height(16.dp))
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun ProgramTable(programs: List<Program>, grade: Int, classNum: Int) {
    Column {
        Text(
            text = "Grade $grade, Class $classNum",
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        RowHeader()

        programs.forEach { program ->
            RowSchedule(program)
        }
    }
}

@Composable
fun RowHeader() {
    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
        listOf("Hour", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday").forEach {
            Text(
                text = it,
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier.weight(1f)
            )
        }
    }
}

@Composable
fun RowSchedule(program: Program) {
    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
        Text(text = program.hour.toString(), modifier = Modifier.weight(1f))
        Text(text = program.monday, modifier = Modifier.weight(1f))
        Text(text = program.tuesday, modifier = Modifier.weight(1f))
        Text(text = program.wednesday, modifier = Modifier.weight(1f))
        Text(text = program.thursday, modifier = Modifier.weight(1f))
        Text(text = program.friday, modifier = Modifier.weight(1f))
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