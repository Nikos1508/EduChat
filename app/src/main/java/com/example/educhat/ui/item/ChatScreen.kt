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
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.Image
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.educhat.R
import com.example.educhat.data.model.Message
import com.example.educhat.data.model.UserProfile
import com.example.educhat.data.network.SupabaseClient.client
import com.example.educhat.ui.components.MessageItemLeft
import com.example.educhat.ui.components.MessageItemRight
import io.github.jan.supabase.auth.auth
import io.github.jan.supabase.postgrest.from
import kotlinx.coroutines.launch

@OptIn(ExperimentalFoundationApi::class, ExperimentalMaterial3Api::class)
@Composable
fun ChatScreen(
    groupId: String,
    modifier: Modifier = Modifier
) {
    var messages by remember { mutableStateOf<List<Message>>(emptyList()) }
    var newMessage by remember { mutableStateOf("") }
    val scope = rememberCoroutineScope()

    val userProfiles = remember { mutableStateMapOf<String, UserProfile>() }

    val currentUserId = remember {
        client.auth.currentUserOrNull()?.id
    }

    suspend fun fetchAllUserProfiles(): List<UserProfile>? {
        return try {
            client.from("user_profiles")
                .select()
                .decodeList<UserProfile>()
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    LaunchedEffect(messages) {
        if (userProfiles.isEmpty()) {
            val profiles = fetchAllUserProfiles()
            profiles?.forEach { profile ->
                userProfiles[profile.id] = profile
            }
        }
    }

    LaunchedEffect(groupId) {
        try {
            val result = client.from("messages")
                .select()
                .decodeList<Message>()

            messages = result.filter { it.group == groupId }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    Scaffold(
        bottomBar = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .imePadding()
                    .padding(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                OutlinedTextField(
                    value = newMessage,
                    onValueChange = { newMessage = it },
                    modifier = Modifier.weight(1f),
                    placeholder = { Text(stringResource(R.string.type_a_message)) },
                    shape = RoundedCornerShape(24.dp),
                    leadingIcon = {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Filled.Image,
                                contentDescription = stringResource(R.string.add_image),
                                modifier = Modifier
                                    .clickable { /* TODO */ }
                                    .padding(start = 6.dp)
                                    .size(30.dp),
                                tint = Color.Gray
                            )
                        }
                    },
                    singleLine = false,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedContainerColor = MaterialTheme.colorScheme.background,
                        unfocusedContainerColor = MaterialTheme.colorScheme.background,
                        disabledContainerColor = MaterialTheme.colorScheme.background
                    )
                )

                Spacer(modifier = Modifier.width(4.dp))

                Button(
                    onClick = {
                        scope.launch {
                            try {
                                val user = client.auth.currentUserOrNull()
                                if (user == null) {
                                    return@launch
                                }

                                val insertedMessage = client.from("messages").insert(
                                    mapOf(
                                        "group" to groupId,
                                        "content" to newMessage,
                                        "sender" to user.id
                                    )
                                ) {
                                    select()
                                    single()
                                }.decodeAs<Message>()

                                messages = messages + insertedMessage
                                newMessage = ""
                            } catch (e: Exception) {
                                e.printStackTrace()
                            }
                        }
                    },
                    shape = CircleShape,
                    contentPadding = PaddingValues(12.dp),
                    modifier = Modifier.size(48.dp)
                ) {
                    Icon(Icons.AutoMirrored.Filled.Send, contentDescription = stringResource(R.string.send))
                }
            }
        }
    ) { innerPadding ->
        LazyColumn(
            contentPadding = innerPadding,
            modifier = Modifier.fillMaxSize()
        ) {
            items(messages, key = { it.id }) { message ->
                val senderProfile = userProfiles[message.sender]
                val senderName = senderProfile?.displayName ?: stringResource(R.string.unknown)
                val timestamp = message.created_at
                val profileImage = senderProfile?.profileImageUrl
                val nameColor = senderProfile?.displayNameColor

                if (message.sender == currentUserId) {
                    MessageItemRight(
                        message = message,
                        senderName = senderName,
                        timestamp = timestamp,
                        senderProfileImageUrl = profileImage,
                        modifier = Modifier.animateItem(fadeInSpec = null, fadeOutSpec = null),
                    )
                } else {
                    MessageItemLeft(
                        message = message,
                        senderName = senderName,
                        timestamp = timestamp,
                        senderProfileImageUrl = profileImage,
                        nameColorHex = nameColor, // ← Pass it here
                        modifier = Modifier.animateItem(fadeInSpec = null, fadeOutSpec = null)
                    )
                }
            }
        }
    }
}