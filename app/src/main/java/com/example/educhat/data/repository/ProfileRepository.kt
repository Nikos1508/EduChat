package com.example.educhat.data.repository

import com.example.educhat.data.model.UserProfile
import com.example.educhat.data.network.SupabaseClient
import io.github.jan.supabase.postgrest.postgrest

class ProfileRepository {

    suspend fun getUserProfile(userId: String): UserProfile? {
        return try {
            val allProfiles = SupabaseClient.client.postgrest
                .from("profiles")
                .select()
                .decodeList<UserProfile>()  // Fetch ALL profiles

            // Now filter locally in Kotlin
            allProfiles.firstOrNull { profile -> profile.id == userId }
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }
}