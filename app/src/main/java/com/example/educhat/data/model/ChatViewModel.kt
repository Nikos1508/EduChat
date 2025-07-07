package com.example.educhat.data.model

/*
import androidx.compose.runtime.mutableStateMapOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.educhat.data.network.SupabaseClient.client
import io.github.jan.supabase.postgrest.from
import kotlinx.coroutines.launch
import io.github.jan.supabase.postgrest.result.PostgrestResult

class ChatViewModel : ViewModel() {
    private val _userProfiles = mutableStateMapOf<String, UserProfile>()
    val userProfiles: Map<String, UserProfile> get() = _userProfiles

    fun fetchUserProfileById(userId: String) {
        viewModelScope.launch {
            val profiles = try {
                client.from("user_profiles")
                    .select()
                    .decode<List<UserProfile>>() // decode a list of profiles
            } catch (e: Exception) {
                e.printStackTrace()
                emptyList()
            }

            val profile = profiles.firstOrNull { it.id == userId }

            profile?.let {
                _userProfiles[userId] = it
            }
        }
    }
}

 */