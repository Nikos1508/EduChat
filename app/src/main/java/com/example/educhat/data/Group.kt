package com.example.educhat.data

import com.example.educhat.ui.model.Group

class Groups() {
    fun loadGroups(): List<Group> {
        return listOf<Group>(
            Group("Group_1", "Blah Blah message 1"),
            Group("Group_2", "Blah Blah message 2"),
            Group("Group_3", "Blah Blah message 3"),
            Group("Group_4", "Blah Blah message 4"),
            Group("Group_5", "Blah Blah message 5, long message so it takes 2 lines."),
            Group("Group_6", "Blah Blah message 6"),
            Group("Group_7", "Blah Blah message 7"),
            Group("Group_8", "Blah Blah message 8"),
            Group("Group_9", "Blah Blah message 9"),
            Group("Group_10", "Blah Blah message 10"),
            Group("Group_11", "Blah Blah message 11"),
            Group("Group_12", "Blah Blah message 12, long message so it takes 2 lines."),
            Group("Group_13", "Blah Blah message 13"),
            Group("Group_14", "Blah Blah message 14")
        )
    }
}