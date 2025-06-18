package com.example.educhat

import android.content.Context
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.educhat.ui.data.network.SupabaseClient.client
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
                _userState.value = UserState.Loading
                client.gotrue.signUpWith(Email) {
                    email = userEmail
                    password = userPassword
                }
                saveToken(context)
                _userState.value = UserState.Success("Registered successfully!")
            } catch (e: Exception) {
                _userState.value = UserState.Error("Error: ${e.message}")
            }

        }
    }

    private fun saveToken(context: Context) {
        viewModelScope.launch {
            val accessToken = client.gotrue.currentAccessTokenOrNull()
            val sharedPref = SharedPreferenceHelper(context)
            sharedPref.saveStringData("accessToken", accessToken)
        }
    }

    private fun getToken(context: Context): String? {
        val sharedPref = SharedPreferenceHelper(context)
        return sharedPref.getStringData("accessToken")
    }

    fun login(
        context: Context,
        userEmail: String,
        userPassword: String,
    ) {
        viewModelScope.launch {
            _userState.value = UserState.Loading
            try {
                _userState.value = UserState.Loading
                client.gotrue.loginWith(Email) {
                    email = userEmail
                    password = userPassword
                }
                saveToken(context)
                _userState.value = UserState.Success("Logged in successfully!")
            } catch (e: Exception) {
                _userState.value = UserState.Error("Error: ${e.message}")
            }
        }
    }

    fun logout(context: Context) {
        val sharedPref = SharedPreferenceHelper(context)
        viewModelScope.launch {
            _userState.value = UserState.Loading
            try {
                _userState.value = UserState.Loading
                client.gotrue.logout()
                sharedPref.clearPreferences()
                _userState.value = UserState.Success("Logged out successfully!")
            } catch (e: Exception) {
                _userState.value = UserState.Error("Error: ${e.message}")
            }
        }
    }

    fun isUserLoggedIn(
        context: Context,
    ) {
        viewModelScope.launch {
            try {
                _userState.value = UserState.Loading
                val token = getToken(context)
                if(token.isNullOrEmpty()) {
                    _userState.value = UserState.Success("User not logged in!")
                } else {
                    client.gotrue.retrieveUser(token)
                    client.gotrue.refreshCurrentSession()
                    saveToken(context)
                    _userState.value = UserState.Success("User already logged in!")
                }
            } catch (e: Exception) {
                _userState.value = UserState.Error("Error: ${e.message}")
            }
        }
    }

}