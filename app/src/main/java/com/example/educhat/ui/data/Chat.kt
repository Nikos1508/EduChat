package com.example.educhat.ui.data

import com.example.educhat.ui.model.Chat

class Chats() {
    fun loadChats(): List<Chat> {
        return listOf<Chat>(
            Chat("Group_1", "Blah Blah message 1"),
            Chat("Group_2", "Blah Blah message 2"),
            Chat("Group_3", "Blah Blah message 3"),
            Chat("Group_4", "Blah Blah message 4"),
            Chat("Group_5", "Blah Blah message 5"),
            Chat("Group_6", "Blah Blah message 6"),
            Chat("Group_7", "Blah Blah message 7")
        )
    }
}