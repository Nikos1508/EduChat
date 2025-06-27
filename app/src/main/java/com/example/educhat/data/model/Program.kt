package com.example.educhat.data.model

import kotlinx.serialization.Serializable

@Serializable
data class Program(
    val id: Int,
    val monday: String,
    val tuesday: String,
    val wednesday: String,
    val thursday: String,
    val friday: String,
    val grade: Int,
    val hour: Int,
    val `class`: Int
)