package com.example.educhat.ui.item

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
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
    var program by remember { mutableStateOf<Program?>(null) }
    var isLoading by remember { mutableStateOf(true) }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    LaunchedEffect(Unit) {
        try {
            val programs = supabase.from("programs")
                .select()
                .decodeList<Program>()

            program = programs.firstOrNull { it.id == 1}
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
        program != null -> {
            Column(
                Modifier
                    .fillMaxSize()
                    .padding(16.dp)
            ) {
                Text("Monday: ${program!!.monday}")
                Text("Tuesday: ${program!!.tuesday}")
                Text("Wednesday: ${program!!.wednesday}")
                Text("Thursday: ${program!!.thursday}")
                Text("Friday: ${program!!.friday}")
                Text("Grade: ${program!!.grade}")
                Text("Hour: ${program!!.hour}")
                Text("Class: ${program!!.`class`}")
            }
        }
        else -> {
            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text("No program found")
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