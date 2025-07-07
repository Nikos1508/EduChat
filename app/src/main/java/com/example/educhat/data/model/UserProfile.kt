package com.example.educhat.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class UserProfile(
    val id: String,

    @SerialName("display_name")
    val displayName: String,

    val description: String? = null,

    @SerialName("profile_image_url")
    val profileImageUrl: String? = null,

    @SerialName("display_name_color")
    val displayNameColor: String? = null
)