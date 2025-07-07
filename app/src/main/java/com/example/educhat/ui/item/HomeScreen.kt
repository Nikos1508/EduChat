package com.example.educhat.ui.item

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.educhat.data.model.Group
import com.example.educhat.data.network.SupabaseClient.client
import com.example.educhat.ui.components.GroupItem
import io.github.jan.supabase.postgrest.from
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchBar(
    query: String,
    onQueryChanged: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    TextField(
        value = query,
        onValueChange = onQueryChanged,
        placeholder = { Text("Search groups") },
        leadingIcon = {
            Icon(
                imageVector = Icons.Default.Search,
                contentDescription = "Search icon",
                tint = MaterialTheme.colorScheme.onSurfaceVariant
            )
        },
        shape = RoundedCornerShape(24.dp),
        colors = TextFieldDefaults.colors(
            focusedContainerColor = MaterialTheme.colorScheme.surfaceVariant,
            unfocusedContainerColor = MaterialTheme.colorScheme.surfaceVariant,
            disabledContainerColor = MaterialTheme.colorScheme.surfaceVariant,
            cursorColor = MaterialTheme.colorScheme.primary,
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent,
            disabledIndicatorColor = Color.Transparent
        ),
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(24.dp))
    )
}

@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    onGroupClick: (String) -> Unit
) {
    var searchQuery by remember { mutableStateOf("") }
    var groups by remember { mutableStateOf<List<String>>(emptyList()) }
    var showAddDialog by remember { mutableStateOf(false) }
    var newChannelName by remember { mutableStateOf("") }

    fun refreshGroups() {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val groupsList = client.from("groups")
                    .select()
                    .decodeList<Group>()

                withContext(Dispatchers.Main) {
                    groups = groupsList.map { it.group }
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    LaunchedEffect(Unit) {
        refreshGroups()
    }

    val filteredGroups = if (searchQuery.isEmpty()) groups else groups.filter {
        it.contains(searchQuery, ignoreCase = true)
    }

    Box(modifier = modifier.fillMaxSize()) {
        Column(modifier = Modifier.padding(4.dp)) {
            SearchBar(
                query = searchQuery,
                onQueryChanged = { searchQuery = it },
                modifier = Modifier.padding(horizontal = 8.dp, vertical = 12.dp)
            )
            GroupList(
                groupList = filteredGroups,
                onGroupClick = onGroupClick,
                modifier = Modifier.fillMaxSize()
            )
        }

        IconButton(
            onClick = { showAddDialog = true },
            modifier = Modifier
                .align(Alignment.BottomStart)
                .padding(16.dp)
                .size(56.dp)
                .background(MaterialTheme.colorScheme.primary, shape = CircleShape)
        ) {
            Icon(
                imageVector = Icons.Default.Add,
                contentDescription = "Add Channel",
                tint = MaterialTheme.colorScheme.onPrimary
            )
        }

        if (showAddDialog) {
            AlertDialog(
                onDismissRequest = {
                    showAddDialog = false
                    newChannelName = ""
                },
                title = { Text("Add Channel") },
                text = {
                    TextField(
                        value = newChannelName,
                        onValueChange = { newChannelName = it },
                        label = { Text("Channel Name") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )
                },
                confirmButton = {
                    TextButton(
                        onClick = {
                            if (newChannelName.isNotBlank()) {
                                CoroutineScope(Dispatchers.IO).launch {
                                    try {
                                        client.from("groups")
                                            .insert(Group(
                                                group = newChannelName.trim()
                                            ))

                                        val updatedGroups = client.from("groups")
                                            .select()
                                            .decodeList<Group>()

                                        withContext(Dispatchers.Main) {
                                            groups = updatedGroups.map { it.group }
                                            showAddDialog = false
                                            newChannelName = ""
                                        }
                                    } catch (e: Exception) {
                                        e.printStackTrace()
                                    }
                                }
                            }
                        }
                    ) {
                        Text("Add")
                    }
                },
                dismissButton = {
                    TextButton(onClick = {
                        showAddDialog = false
                        newChannelName = ""
                    }) {
                        Text("Cancel")
                    }
                }
            )
        }
    }
}

@Composable
fun GroupList(
    groupList: List<String>,
    modifier: Modifier = Modifier,
    onGroupClick: (String) -> Unit
) {
    LazyColumn(modifier = modifier) {
        items(groupList) { groupName ->
            GroupItem(
                groupTitle = groupName,
                modifier = Modifier.padding(8.dp),
                onClick = { onGroupClick(groupName) }
            )
        }
    }
}