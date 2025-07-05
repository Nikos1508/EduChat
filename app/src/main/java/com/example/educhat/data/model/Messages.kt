package com.example.educhat.data.model

import kotlinx.serialization.Serializable

@Serializable
data class Message(
    val id: Int,
    val group: String,
    val content: String,
    val sender: String,
    val created_at: String
)