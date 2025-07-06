package com.example.educhat.data.model

import kotlinx.serialization.Serializable

@Serializable
data class Group(
    val id: Int,
    val group: String
)
@Serializable
data class Message(
    val id: Int,
    val content: String,
    val sender: String,
    val group: String,
    val created_at: String
)