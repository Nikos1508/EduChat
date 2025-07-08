package com.example.educhat

import android.content.ContentResolver
import android.content.Context
import android.net.Uri
import android.util.Log
import android.widget.Toast
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.educhat.data.model.UserProfile
import com.example.educhat.data.model.UserState
import com.example.educhat.data.network.SupabaseClient.client
import com.example.educhat.data.repository.ProfileRepository
import com.example.educhat.utils.SharedPreferenceHelper
import io.github.jan.supabase.auth.auth
import io.github.jan.supabase.exceptions.HttpRequestException
import io.github.jan.supabase.exceptions.RestException
import io.github.jan.supabase.postgrest.postgrest
import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.header
import io.ktor.client.request.patch
import io.ktor.client.request.put
import io.ktor.client.request.setBody
import io.ktor.client.statement.HttpResponse
import io.ktor.client.statement.bodyAsText
import io.ktor.http.ContentType
import io.ktor.http.contentType
import io.ktor.http.isSuccess
import io.ktor.serialization.kotlinx.json.json
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.put


class SupabaseAuthViewModel : ViewModel() {

    private val profileRepository = ProfileRepository()

    private val _userState = mutableStateOf<UserState>(UserState.Success(""))
    val userState: State<UserState> = _userState

    private val _userProfile = mutableStateOf<UserProfile?>(null)
    val userProfile: State<UserProfile?> = _userProfile

    private val _currentUserEmail = mutableStateOf<String?>(null)
    val currentUserEmail: State<String?> = _currentUserEmail

    private val httpClient = HttpClient(CIO) {
        install(ContentNegotiation) {
            json(Json { ignoreUnknownKeys = true })
        }
    }

    var resetToken by mutableStateOf<String?>(null)
        private set

    fun updateResetToken(token: String?) {
        resetToken = token
    }

    fun setUserState(state: UserState) {
        _userState.value = state
    }

    private fun saveTokens(context: Context) {
        viewModelScope.launch {
            val session = client.auth.currentSessionOrNull()
            val sharedPref = SharedPreferenceHelper(context)
            sharedPref.saveStringData("accessToken", session?.accessToken)
            sharedPref.saveStringData("refreshToken", session?.refreshToken)
            Log.d("AuthVM", "Tokens saved.")
        }
    }

    private fun clearTokens(context: Context) {
        val sharedPref = SharedPreferenceHelper(context)
        sharedPref.clearPreferences()
        Log.d("AuthVM", "Tokens cleared.")
    }

    fun loadCurrentUserEmail() {
        val user = client.auth.currentUserOrNull()
        _currentUserEmail.value = user?.email
    }

    suspend fun ensureUserProfileExists(): Boolean {
        val user = client.auth.currentUserOrNull() ?: return false
        val profile = profileRepository.getUserProfile(user.id)
        return if (profile == null) {
            createProfile(user.email ?: "Unknown")
        } else {
            true
        }
    }

    suspend fun refreshSessionManually(context: Context): Boolean = withContext(Dispatchers.IO) {
        val sharedPref = SharedPreferenceHelper(context)
        val refreshToken = sharedPref.getStringData("refreshToken")

        if (refreshToken.isNullOrEmpty()) {
            _userState.value = UserState.Success("User not logged in!")
            return@withContext false
        }

        try {
            client.auth.refreshCurrentSession()

            val currentUser = client.auth.currentUserOrNull()
            if (currentUser != null) {
                saveTokens(context)
                loadCurrentUserEmail()
                _userState.value = UserState.Success("User already logged in!")
                true
            } else {
                clearTokens(context)
                _currentUserEmail.value = null
                _userState.value = UserState.Success("User not logged in!")
                false
            }
        } catch (e: Exception) {
            clearTokens(context)
            _currentUserEmail.value = null
            _userState.value = UserState.Error("Session expired. Please log in.")
            false
        }
    }

    suspend fun updateThemeColor(newColor: String): Boolean {
        val user = client.auth.currentUserOrNull() ?: return false
        val token = client.auth.currentSessionOrNull()?.accessToken ?: return false

        val body = buildJsonObject {
            put("theme_color", newColor)
        }

        val url = "${BuildConfig.supabaseUrl}/rest/v1/profiles?id=eq.${user.id}"

        return try {
            val response: HttpResponse = httpClient.patch(url) {
                header("apikey", BuildConfig.supabaseKey)
                header("Authorization", "Bearer $token")
                contentType(ContentType.Application.Json)
                setBody(body)
            }
            response.status.isSuccess()
        } catch (e: Exception) {
            Log.e("updateThemeColor", "Failed to update color", e)
            false
        }
    }

    private fun generateRandomColor(): String {
        val colors = listOf(
            "#FF5733", "#33FF57", "#3357FF",
            "#FF33A1", "#FF8F33", "#33FFF2",
            "#A633FF", "#FF3333", "#33FFBD",
            "#FFD433", "#33A1FF", "#8FFF33",
            "#FF3380", "#33FFD4", "#7A33FF",
            "#FF7A33", "#33FF7A", "#FF3357",
            "#5733FF", "#33FFDA", "#FFDA33"
        )
        return colors.random()
    }

    suspend fun createProfile(displayName: String): Boolean = withContext(Dispatchers.IO) {
        val user = client.auth.currentUserOrNull() ?: return@withContext false
        val userId = user.id
        val color = generateRandomColor()

        val profileData = mapOf(
            "id" to userId,
            "display_name" to displayName,
            "description" to null,
            "profile_image_url" to null,
            "display_name_color" to color
        )

        try {
            client.postgrest["profiles"].insert(profileData)
            Log.d("createProfile", "Profile created successfully with color $color")
            true
        } catch (e: Exception) {
            Log.e("createProfile", "Exception while creating profile", e)
            false
        }
    }

    fun loadUserProfile() {
        client.auth.currentUserOrNull()?.let { user ->
            viewModelScope.launch {
                val profile = profileRepository.getUserProfile(user.id)
                _userProfile.value = profile
            }
        }
    }

    suspend fun updateProfile(
        newDisplayName: String? = null,
        newDescription: String? = null,
        newImageUrl: String? = null
    ): Boolean {
        val user = client.auth.currentUserOrNull() ?: return false
        val token = client.auth.currentSessionOrNull()?.accessToken ?: return false

        val body = buildJsonObject {
            newDisplayName?.takeIf { it.isNotBlank() }?.let { put("display_name", it) }
            newDescription?.takeIf { it.isNotBlank() }?.let { put("description", it) }
            newImageUrl?.let { put("profile_image_url", it) }
        }

        val url = "${BuildConfig.supabaseUrl}/rest/v1/profiles?id=eq.${user.id}"

        return try {
            val response: HttpResponse = httpClient.patch(url) {
                header("apikey", BuildConfig.supabaseKey)
                header("Authorization", "Bearer $token")
                contentType(ContentType.Application.Json)
                setBody(body)
            }
            response.status.isSuccess()
        } catch (e: Exception) {
            Log.e("updateProfile", "Update failed", e)
            false
        }
    }

    suspend fun uploadProfileImage(uri: Uri, resolver: ContentResolver): String? {
        val user = client.auth.currentUserOrNull() ?: return null
        val token = client.auth.currentSessionOrNull()?.accessToken ?: return null

        val bucket = "profile-images"
        val fileName = "profile.png"
        val path = "${user.id}/$fileName"

        val bytes = resolver.openInputStream(uri)?.use { it.readBytes() } ?: return null

        val url = "${BuildConfig.supabaseUrl}/storage/v1/object/$bucket/$path"

        return try {
            val response: HttpResponse = httpClient.put(url) {
                header("apikey", BuildConfig.supabaseKey)
                header("Authorization", "Bearer $token")
                contentType(ContentType.Image.PNG)
                setBody(bytes)
            }

            if (response.status.isSuccess()) {
                "${BuildConfig.supabaseUrl}/storage/v1/object/public/$bucket/$path"
            } else {
                null
            }
        } catch (e: Exception) {
            Log.e("uploadProfileImage", "Upload failed", e)
            null
        }
    }

    fun uploadAndSetProfileImage(uri: Uri, resolver: ContentResolver) {
        viewModelScope.launch {
            _userState.value = UserState.Loading
            val publicUrl = uploadProfileImage(uri, resolver)
            if (publicUrl != null) {
                val success = updateProfile(newImageUrl = publicUrl)
                if (success) {
                    loadUserProfile()  // Refresh profile so UI shows new image
                    _userState.value = UserState.Success("Profile image updated successfully!")
                } else {
                    _userState.value = UserState.Error("Failed to update profile image URL.")
                }
            } else {
                _userState.value = UserState.Error("Failed to upload image.")
            }
        }
    }

    fun signUp(context: Context, userEmail: String, userPassword: String, displayName: String, onNavigateToLogin: () -> Unit) {
        viewModelScope.launch {
            _userState.value = UserState.Loading
            try {
                val result = client.auth.signUpWith(io.github.jan.supabase.auth.providers.builtin.Email) {
                    email = userEmail
                    password = userPassword
                }

                val user = client.auth.currentUserOrNull()
                val emailConfirmed = user?.emailConfirmedAt != null

                if (!emailConfirmed) {
                    _userState.value = UserState.Success("Check your email to confirm your account.")
                    onNavigateToLogin()
                    return@launch
                }

                val success = createProfile(displayName)
                if (!success) {
                    _userState.value = UserState.Error("Failed to create user profile.")
                    return@launch
                }

                saveTokens(context)
                loadCurrentUserEmail()
                _userState.value = UserState.Success("Registered successfully!")
                onNavigateToLogin()

            } catch (e: Exception) {
                val errorMessage = when (e) {
                    is RestException -> e.error ?: "Registration failed. Server error."
                    is HttpRequestException -> "Network error during registration. Please check connection."
                    else -> e.message ?: "Unknown registration error."
                }
                _userState.value = UserState.Error(errorMessage)
            }
        }
    }


    fun login(context: Context, userEmail: String, userPassword: String) {
        viewModelScope.launch {
            _userState.value = UserState.Loading
            try {
                client.auth.signInWith(io.github.jan.supabase.auth.providers.builtin.Email) {
                    email = userEmail
                    password = userPassword
                }

                val profileCreated = ensureUserProfileExists()

                saveTokens(context)
                loadCurrentUserEmail()
                loadUserProfile()

                if (profileCreated) {
                    _userState.value = UserState.Success("Logged in successfully!")
                } else {
                    _userState.value = UserState.Error("Failed to create user profile.")
                }

            } catch (e: Exception) {
                val errorMessage = when (e) {
                    is RestException -> e.error ?: "Login failed. Invalid credentials or server error."
                    is HttpRequestException -> "Network error during login. Please check your connection."
                    else -> e.message ?: "Unknown login error."
                }
                _userState.value = UserState.Error(errorMessage)
            }
        }
    }

    fun logout(context: Context) {
        viewModelScope.launch {
            _userState.value = UserState.Loading
            try {
                client.auth.signOut()
                clearTokens(context)
                _currentUserEmail.value = null
                _userProfile.value = null
                _userState.value = UserState.Success("Logged out successfully!")
            } catch (e: Exception) {
                _userState.value = UserState.Error(e.message ?: "Logout failed.")
            }
        }
    }

    fun sendPasswordResetEmail(context: Context, email: String) {
        viewModelScope.launch {
            _userState.value = UserState.Loading
            try {
                client.auth.resetPasswordForEmail(email)
                _userState.value = UserState.Success("Password reset email sent. Check your inbox.")
                Toast.makeText(context, "Password reset email sent. Check your inbox.", Toast.LENGTH_LONG).show()
            } catch (e: Exception) {
                _userState.value = UserState.Error(e.localizedMessage ?: "Unknown error")
                Toast.makeText(context, "Error: ${e.localizedMessage}", Toast.LENGTH_LONG).show()
            }
        }
    }


    suspend fun updatePasswordWithToken(newPassword: String, context: Context, onSuccess: () -> Unit) {
        val token = resetToken
        if (token.isNullOrEmpty()) {
            Toast.makeText(context, "Invalid or expired reset token", Toast.LENGTH_LONG).show()
            return
        }

        _userState.value = UserState.Loading

        try {
            val url = "${BuildConfig.supabaseUrl}/auth/v1/user"

            val body = buildJsonObject {
                put("password", newPassword)
            }

            val response: HttpResponse = httpClient.patch(url) {
                header("apikey", BuildConfig.supabaseKey)
                header("Authorization", "Bearer $token")
                contentType(ContentType.Application.Json)
                setBody(body)
            }

            if (response.status.isSuccess()) {
                _userState.value = UserState.Success("Password updated successfully")
                Toast.makeText(context, "Password updated successfully", Toast.LENGTH_LONG).show()
                resetToken = null
                onSuccess()
            } else {
                val errorText = response.bodyAsText()
                _userState.value = UserState.Error("Failed to update password: $errorText")
                Toast.makeText(context, "Failed to update password: $errorText", Toast.LENGTH_LONG).show()
            }

        } catch (e: Exception) {
            _userState.value = UserState.Error(e.localizedMessage ?: "Unknown error")
            Toast.makeText(context, "Error: ${e.localizedMessage}", Toast.LENGTH_LONG).show()
        }
    }

    fun isUserLoggedIn(context: Context) {
        viewModelScope.launch {
            _userState.value = UserState.Loading
            val success = refreshSessionManually(context)
            if (success) {
                val profileExists = ensureUserProfileExists()

                if (profileExists) {
                    loadUserProfile()
                    _userState.value = UserState.Success("User logged in!")
                } else {
                    _userState.value = UserState.Error("Failed to create user profile.")
                }
            } else {
                clearTokens(context)
                _currentUserEmail.value = null
                _userProfile.value = null
                _userState.value = UserState.Success("User not logged in!")
            }
        }
    }
}