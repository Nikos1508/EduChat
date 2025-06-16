package com.example.educhat

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.educhat.ui.components.Keyboard
import com.example.educhat.ui.theme.EduChatTheme
import io.github.jan.supabase.createSupabaseClient
import io.github.jan.supabase.postgrest.Postgrest
import io.github.jan.supabase.postgrest.from
import kotlinx.serialization.Serializable

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            EduChatTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    NotesList(Modifier.padding(innerPadding))
                }
            }
        }
    }
}

val supabase = createSupabaseClient(
    supabaseUrl = "https://lrqclmhdgdyvcechgxrq.supabase.co",
    supabaseKey = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJzdXBhYmFzZSIsInJlZiI6ImxycWNsbWhkZ2R5dmNlY2hneHJxIiwicm9sZSI6ImFub24iLCJpYXQiOjE3NDI3MTYxNTYsImV4cCI6MjA1ODI5MjE1Nn0.o9tMHAvC9s2WhzB4gT1-LTUYhuBamStRh4aee9DaHTI"
) {
    install(Postgrest)
}

@Serializable
data class Note(
    val id: Int,
    val body: String
)

@Composable
fun NotesList(modifier: Modifier = Modifier) {
    var notes by remember { mutableStateOf<List<Note>>(emptyList()) }

    LaunchedEffect(Unit) {
        try {
            val result = supabase.from("notes")
                .select()
                .decodeList<Note>()
            notes = result
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    Column {
        LazyColumn(modifier = modifier) {
            items(notes, key = { it.id }) { note ->
                Text(
                    text = note.body,
                    modifier = Modifier.padding(16.dp)
                )
            }
        }
    }
}


@Composable
fun EduChatApp(modifier: Modifier) {
    Keyboard(modifier = modifier)
}

@Preview(showBackground = true)
@Composable
fun EduChatAppPreviewLight() {
    EduChatTheme {
        NotesList(modifier = Modifier)
    }
}

@Preview(showBackground = true)
@Composable
fun EduChatAppPreviewDark() {
    EduChatTheme (darkTheme = true) {
       NotesList(modifier = Modifier)
    }
}