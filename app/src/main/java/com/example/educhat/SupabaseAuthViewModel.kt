package com.example.educhat

import android.content.Context
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.educhat.data.network.SupabaseClient.client
import com.example.educhat.ui.model.UserState
import com.example.educhat.ui.utils.SharedPreferenceHelper
import io.github.jan.supabase.gotrue.gotrue
import io.github.jan.supabase.gotrue.providers.builtin.Email
import kotlinx.coroutines.launch

class SupabaseAuthViewModel : ViewModel() {
    private val _userState = mutableStateOf<UserState>(UserState.Loading)
    val userState: State<UserState> = _userState

    fun signUp(
        context: Context,
        userEmail: String,
        userPassword: String,
    ) {
        viewModelScope.launch {
            _userState.value = UserState.Loading
            try {
                client.gotrue.signUpWith(Email) {
                    email = userEmail
                    password = userPassword
                }

                // Get token after sign up
                val accessToken = client.gotrue.currentAccessTokenOrNull()

                if (!accessToken.isNullOrEmpty()) {
                    saveToken(context, accessToken)
                    _userState.value = UserState.Success("Registered successfully!")
                } else {
                    _userState.value = UserState.Error("Sign up failed: No access token received")
                }
            } catch (e: Exception) {
                _userState.value = UserState.Error("Error: ${e.message ?: "Unknown error"}")
            }
        }
    }

    fun login(
        context: Context,
        userEmail: String,
        userPassword: String,
    ) {
        viewModelScope.launch {
            _userState.value = UserState.Loading
            try {
                client.gotrue.loginWith(Email) {
                    email = userEmail
                    password = userPassword
                }

                val accessToken = client.gotrue.currentAccessTokenOrNull()

                if (!accessToken.isNullOrEmpty()) {
                    saveToken(context, accessToken)
                    _userState.value = UserState.Success("Logged in successfully!")
                } else {
                    _userState.value = UserState.Error("Login failed: No access token received")
                }
            } catch (e: Exception) {
                _userState.value = UserState.Error("Error: ${e.message ?: "Unknown error"}")
            }
        }
    }

    private fun saveToken(context: Context, token: String) {
        val sharedPref = SharedPreferenceHelper(context)
        sharedPref.saveStringData("accessToken", token)
    }

    private fun getToken(context: Context): String? {
        val sharedPref = SharedPreferenceHelper(context)
        return sharedPref.getStringData("accessToken")
    }

    fun logout(context: Context) {
        viewModelScope.launch {
            _userState.value = UserState.Loading
            try {
                client.gotrue.logout()
                val sharedPref = SharedPreferenceHelper(context)
                sharedPref.clearPreferences()
                _userState.value = UserState.Success("Logged out successfully!")
            } catch (e: Exception) {
                _userState.value = UserState.Error("Error: ${e.message ?: "Unknown error"}")
            }
        }
    }

    fun isUserLoggedIn(context: Context) {
        viewModelScope.launch {
            _userState.value = UserState.Loading
            try {
                val token = getToken(context)
                if (token.isNullOrEmpty()) {
                    _userState.value = UserState.Success("User not logged in!")
                } else {
                    client.gotrue.retrieveUser(token) // throws if invalid
                    client.gotrue.refreshCurrentSession()
                    saveToken(context, client.gotrue.currentAccessTokenOrNull() ?: "")
                    _userState.value = UserState.Success("User already logged in!")
                }
            } catch (e: Exception) {
                _userState.value = UserState.Error("Error: ${e.message ?: "Unknown error"}")
            }
        }
    }
}