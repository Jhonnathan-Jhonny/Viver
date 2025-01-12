package com.project.viver
import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.project.viver.data.models.OrderUiStateUser
import com.project.viver.data.models.UserState
import com.project.viver.data.network.SupabaseClient.supabase
import com.project.viver.utils.SharedPreferenceHelper
import io.github.jan.supabase.exceptions.RestException
import io.github.jan.supabase.gotrue.auth
import io.github.jan.supabase.gotrue.providers.builtin.Email
import io.github.jan.supabase.postgrest.from
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class ViverViewModel : ViewModel() {

    private val _uiState = MutableStateFlow<UserState>(UserState.Loading)
    val uiState: StateFlow<UserState> = _uiState

    fun signUpUser(context: Context, user: OrderUiStateUser) {
        viewModelScope.launch {
            try {
                val response = supabase.auth.signUpWith(Email) {
                    email = user.email
                    password = user.password
                }
                supabase
                    .from("users")
                    .insert(
                        mapOf(
                            "idAuth" to "response.id",
                            "name" to user.name,
                            "surname" to user.surname,
                            "sex" to user.sex
                        )
                    )
                _uiState.value = UserState.Success("User registered successfully")
                saveToken(context)
            } catch (e: Exception) {
                _uiState.value = UserState.Error("Error during registration: ${e.message}")
            }
        }
    }


    private fun saveToken(context: Context) {
        viewModelScope.launch {
            val accessToken = supabase.auth.currentAccessTokenOrNull()
            val sharedPref = SharedPreferenceHelper(context)
            sharedPref.saveStringData("accessToken",accessToken)
        }
    }

    private fun getToken(context: Context): String? {
        val sharedPref = SharedPreferenceHelper(context)
        return sharedPref.getStringData("accessToken")
    }

    fun logInUser(context: Context, email: String, password: String) {
        viewModelScope.launch {
            try {
                supabase.auth.signInWith(Email) {
                    this.email = email
                    this.password = password
                }
                saveToken(context)
                _uiState.value = UserState.Success("Logged in user successfully")
            } catch (e: Exception) {
                _uiState.value = UserState.Error("Error during login: ${e.message}")
            }
        }
    }

    fun logOutUser(context: Context) {
        viewModelScope.launch {
            try {
                supabase.auth.signOut()
                _uiState.value = UserState.Success("Logged out user successfully")
            } catch (e: Exception) {
                _uiState.value = UserState.Error("Error during logout: ${e.message}")
            }
        }
    }

    fun isUserLoggedIn(
        context: Context,
    ) {
        viewModelScope.launch {
            try {
                _uiState.value = UserState.Loading
                val token = getToken(context)
                if(token.isNullOrEmpty()) {
                    _uiState.value = UserState.Success("User not logged in!")
                } else {
                    supabase.auth.retrieveUser(token)
                    supabase.auth.refreshCurrentSession()
                    saveToken(context)
                    _uiState.value = UserState.Success("User already logged in!")
                }
            } catch (e: RestException) {
                _uiState.value = UserState.Error(e.error)
            }
        }
    }
}
