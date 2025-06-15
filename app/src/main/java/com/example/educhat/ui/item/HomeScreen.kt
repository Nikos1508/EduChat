package com.example.educhat.ui.item

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.educhat.ui.components.GroupItem
import com.example.educhat.ui.components.TopBar
import com.example.educhat.ui.data.Groups
import com.example.educhat.ui.model.Group

@Composable
fun HomeScreen(modifier: Modifier) {
    Scaffold(
        topBar = {
            TopBar(modifier = modifier)
        },
    ) { innerPadding ->
        GroupList(
            groupList = Groups().loadGroups(),
            modifier = Modifier.padding((innerPadding))
        )
    }
}

@Composable
fun GroupList(groupList: List<Group>, modifier: Modifier = Modifier) {
    LazyColumn(modifier = modifier) {
        items(groupList) { group ->
            GroupItem(
                group = group,
                modifier = Modifier.padding(8.dp)
            )
        }
    }
}