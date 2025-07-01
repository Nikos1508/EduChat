package com.example.educhat.data.model

import kotlinx.serialization.Serializable

@Serializable
data class CalendarEvent(
    val id: Int,
    val event: String,
    val description: String,
    val date: String,
    val type: String? = null
)