package com.example.educhat

import android.content.Context
import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.educhat.data.model.UserState
import com.example.educhat.data.network.SupabaseClient.client
import com.example.educhat.utils.SharedPreferenceHelper
import io.github.jan.supabase.auth.auth
import io.github.jan.supabase.auth.providers.builtin.Email
import io.github.jan.supabase.exceptions.HttpRequestException
import io.github.jan.supabase.exceptions.RestException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class SupabaseAuthViewModel : ViewModel() {

    private val _userState = mutableStateOf<UserState>(UserState.Success(""))
    val userState: State<UserState> = _userState

    private val _currentUserEmail = mutableStateOf<String?>(null)
    val currentUserEmail: State<String?> = _currentUserEmail

    /**
     * Save current session tokens into SharedPreferences.
     */
    private fun saveTokens(context: Context) {
        viewModelScope.launch {
            val session = client.auth.currentSessionOrNull()
            val sharedPref = SharedPreferenceHelper(context)
            sharedPref.saveStringData("accessToken", session?.accessToken)
            sharedPref.saveStringData("refreshToken", session?.refreshToken)
            Log.d("AuthVM", "Tokens saved.")
        }
    }

    /**
     * Clear tokens from SharedPreferences.
     */
    private fun clearTokens(context: Context) {
        val sharedPref = SharedPreferenceHelper(context)
        sharedPref.clearPreferences()
        Log.d("AuthVM", "Tokens cleared.")
    }

    /**
     * Load current user's email from Supabase client.
     */
    fun loadCurrentUserEmail() {
        val user = client.auth.currentUserOrNull()
        _currentUserEmail.value = user?.email
    }

    /**
     * Attempt to refresh session tokens manually using stored refresh token.
     * Returns true if refreshed successfully, false otherwise.
     */
    suspend fun refreshSessionManually(context: Context): Boolean = withContext(Dispatchers.IO) {
        val sharedPref = SharedPreferenceHelper(context)
        val refreshToken = sharedPref.getStringData("refreshToken")

        if (refreshToken.isNullOrEmpty()) {
            _userState.value = UserState.Success("User not logged in!")
            return@withContext false
        }

        try {
            // Supabase client does not provide explicit refresh call in SDK,
            // so we can invoke the refreshCurrentSession() to let SDK handle refresh internally.
            client.auth.refreshCurrentSession()

            val currentUser = client.auth.currentUserOrNull()
            if (currentUser != null) {
                saveTokens(context)  // Save new tokens after refresh
                loadCurrentUserEmail()
                _userState.value = UserState.Success("User already logged in!")
                return@withContext true
            } else {
                clearTokens(context)
                _currentUserEmail.value = null
                _userState.value = UserState.Success("User not logged in!")
                return@withContext false
            }
        } catch (e: Exception) {
            clearTokens(context)
            _currentUserEmail.value = null
            _userState.value = UserState.Error("Session expired. Please log in.")
            return@withContext false
        }
    }

    fun signUp(context: Context, userEmail: String, userPassword: String) {
        viewModelScope.launch {
            _userState.value = UserState.Loading
            try {
                client.auth.signUpWith(Email) {
                    email = userEmail
                    password = userPassword
                }
                saveTokens(context)
                loadCurrentUserEmail()
                _userState.value = UserState.Success("Registered successfully! Check your email.")
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
                client.auth.signInWith(Email) {
                    email = userEmail
                    password = userPassword
                }
                saveTokens(context)
                loadCurrentUserEmail()
                _userState.value = UserState.Success("Logged in successfully!")
            } catch (e: Exception) {
                val errorMessage = when (e) {
                    is RestException -> e.error ?: "Login failed. Invalid credentials or server error."
                    is HttpRequestException -> "Network error during login. Please check connection."
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
                _userState.value = UserState.Success("Logged out successfully!")
            } catch (e: Exception) {
                _userState.value = UserState.Error(e.message ?: "Logout failed.")
            }
        }
    }

    /**
     * Checks if user is logged in by trying to refresh session tokens.
     * Should be called on app start to skip login screen if valid token exists.
     */
    fun isUserLoggedIn(context: Context) {
        viewModelScope.launch {
            _userState.value = UserState.Loading
            val success = refreshSessionManually(context)
            if (!success) {
                clearTokens(context)
                _currentUserEmail.value = null
                _userState.value = UserState.Success("User not logged in!")
            }
        }
    }
}