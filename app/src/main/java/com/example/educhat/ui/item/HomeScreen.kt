package com.example.educhat.ui.item

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.educhat.data.model.Group
import com.example.educhat.data.network.SupabaseClient.client
import com.example.educhat.ui.components.GroupItem
import io.github.jan.supabase.postgrest.from

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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    onGroupClick: (String) -> Unit
) {
    var searchQuery by remember { mutableStateOf("") }
    var groups by remember { mutableStateOf<List<String>>(emptyList()) }

    LaunchedEffect(Unit) {
        try {
            val groupsList = client.from("groups")
                .select()
                .decodeList<Group>()

            println("Fetched groups: $groupsList")
            groups = groupsList.map { it.group }
            println("Mapped groups: $groups")

        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    val filteredGroups = if (searchQuery.isEmpty()) groups else groups.filter {
        it.contains(searchQuery, ignoreCase = true)
    }

    Column(modifier = modifier.padding(4.dp)) {
        SearchBar(
            query = searchQuery,
            onQueryChanged = { searchQuery = it },
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 12.dp)
        )
        GroupList(
            groupList = filteredGroups,
            onGroupClick = onGroupClick
        )
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


@Preview(showBackground = true)
@Composable
fun HomeScreenLightPreview() {
    HomeScreen(onGroupClick = {})
}

@Preview(showBackground = true)
@Composable
fun HomeScreenDarkPreview() {
    HomeScreen(onGroupClick = {})
}