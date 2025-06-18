package com.example.educhat.ui.item

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.EmojiEmotions
import androidx.compose.material.icons.filled.Image
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.educhat.ui.components.MessageItem
import com.example.educhat.ui.theme.EduChatTheme
import io.github.jan.supabase.createSupabaseClient
import io.github.jan.supabase.postgrest.Postgrest
import io.github.jan.supabase.postgrest.from
import kotlinx.coroutines.launch
import kotlinx.serialization.Serializable

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

@OptIn(ExperimentalFoundationApi::class, ExperimentalMaterial3Api::class)
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
    Scaffold(
        bottomBar = {
            var newNote by remember { mutableStateOf("") }
            val scope = rememberCoroutineScope()

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .imePadding()
                    .padding(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                OutlinedTextField(
                    value = newNote,
                    onValueChange = { newNote = it },
                    modifier = Modifier.weight(1f),
                    placeholder = { Text("Type a message...") },
                    shape = RoundedCornerShape(24.dp),
                    leadingIcon = {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Filled.Image,
                                contentDescription = "Add Image",
                                modifier = Modifier
                                    .clickable {  }
                                    .padding(start = 6.dp)
                                    .size(30.dp),
                                tint = Color.Gray
                            )
                            Icon(
                                imageVector = Icons.Filled.EmojiEmotions,
                                contentDescription = "Add Sticker",
                                modifier = Modifier
                                    .clickable {  }
                                    .padding(end = 6.dp)
                                    .size(30.dp),
                                tint = Color.Gray
                            )
                        }
                    },
                    singleLine = false,
                    colors = TextFieldDefaults.outlinedTextFieldColors(
                        containerColor = MaterialTheme.colorScheme.background
                    )
                )

                Spacer(modifier = Modifier.width(4.dp))

                Button(
                    onClick = {
                        scope.launch {
                            try {
                                val note = supabase.from("notes").insert(mapOf("body" to newNote)) {
                                    select()
                                    single()
                                }.decodeAs<Note>()

                                notes = notes + note
                                newNote = ""
                            } catch (e: Exception) {
                                e.printStackTrace()
                            }
                        }
                    },
                    shape = CircleShape,
                    contentPadding = PaddingValues(12.dp),
                    modifier = Modifier.size(48.dp)
                ) {
                    Icon(Icons.Default.Send, contentDescription = "Send")
                }
            }
        }
    ) { innerPadding ->
        LazyColumn(
            contentPadding = innerPadding,
            modifier = Modifier.fillMaxSize()
        ) {
            items(notes, key = { it.id }) { note ->
                MessageItem(
                    text = note.body,
                    modifier = Modifier.animateItemPlacement()
                )
            }
        }
    }
}

@Composable
fun ChatScreen(modifier: Modifier = Modifier) {
    NotesList(modifier = modifier)
}

@Preview(showBackground = true)
@Composable
fun MessagePreviewLight() {
    EduChatTheme {
        ChatScreen(modifier = Modifier.padding(4.dp))
    }
}

@Preview
@Composable
fun MessagePreviewDark() {
    EduChatTheme(darkTheme = true) {
        ChatScreen(modifier = Modifier.padding(4.dp))
    }
}