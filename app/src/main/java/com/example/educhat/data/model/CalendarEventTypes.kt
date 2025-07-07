package com.example.educhat.data.model

enum class CalendarEventType(val displayName: String) {
    Meeting("Meeting"),
    Exam("Exam"),
    Personal("Personal");

    companion object {
        fun fromString(value: String?): CalendarEventType? {
            return values().find { it.displayName.equals(value, ignoreCase = true) }
        }
    }
}