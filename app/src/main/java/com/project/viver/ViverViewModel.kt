package com.project.viver
import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
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
import io.github.jan.supabase.postgrest.query.Columns
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

open class ViverViewModel : ViewModel() {

    private val _uiState = MutableStateFlow<UserState>(UserState.Loading)
    val uiState: StateFlow<UserState> = _uiState

    private val _userProfile = MutableLiveData<OrderUiStateUser>()
    val userProfile: LiveData<OrderUiStateUser> = _userProfile

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

    fun refreshTokenIfNeeded() {
        viewModelScope.launch {
            _uiState.value = UserState.Loading
            try {
                // Simule o processo de refresh do token
                supabase.auth.refreshCurrentSession()
                _uiState.value = UserState.Success("Token refreshed successfully")
            } catch (e: Exception) {
                _uiState.value = UserState.Error("Unknown error")
            }
        }
    }

    fun fetchUserProfile(context: Context) {
        viewModelScope.launch {
            _uiState.value = UserState.Loading
            try {
                val token = getToken(context)
                if (token.isNullOrEmpty()) {
                    _uiState.value = UserState.Error("Token não encontrado.")
                    return@launch
                }

                val userId = supabase.auth.retrieveUser(token).id
                val response = supabase
                    .from("users")
                    .select(columns = Columns.list("name, surname, sex, restrictions")) {
                        filter {
                            eq("idAuth", userId)
                        }
                    }
                    .decodeSingle<OrderUiStateUser>()

                _userProfile.value = response

                _uiState.value = UserState.Success("Perfil do usuário carregado com sucesso.")
            } catch (e: Exception) {
                _uiState.value = UserState.Error("Erro ao buscar perfil: ${e.message ?: "Erro desconhecido"}")
            }
        }
    }

    open suspend fun signUpUser(context: Context, user: OrderUiStateUser): UserState {
        return try {
            // Registrar o usuário
            val response = supabase.auth.signUpWith(Email) {
                email = user.email
                password = user.password
            }

            // Recuperar o ID do usuário
            val userId = response?.id ?: supabase.auth.currentSessionOrNull()?.user?.id

            if (userId == null) {
                _uiState.value = UserState.Error("Failed to retrieve user ID after signup")
                throw Exception("Failed to retrieve user ID after signup")
            }

            // Inserir no banco de dados
            supabase
                .from("users")
                .insert(
                    mapOf(
                        "idAuth" to userId,
                        "name" to user.name,
                        "surname" to user.surname,
                        "sex" to user.sex
                    )
                )

            _uiState.value = UserState.Success("User registered successfully")
            saveToken(context)
            UserState.Success("User registered successfully")
        } catch (e: Exception) {
            _uiState.value = UserState.Error("Error during registration: ${e.message}")
            UserState.Error("Error during registration: ${e.message}")
        }
    }


    fun checkIfUserLoggedIn(context: Context): Boolean {
        val token = getToken(context)
        return !token.isNullOrEmpty()
    }

    suspend fun logInUser(context: Context, email: String, password: String): UserState {
        return try {
            supabase.auth.signInWith(Email) {
                this.email = email
                this.password = password
            }
            _uiState.value = UserState.Success("Logged in user successfully")
            saveToken(context)
            UserState.Success("Logged in user successfully")
        } catch (e: Exception) {
            _uiState.value = UserState.Error("Error during login: ${e.message}")
            UserState.Error("Usuário inexistente ou email ou senha incorreta")
        }
    }

    fun logOutUser(context: Context) {
        viewModelScope.launch {
            try {
                supabase.auth.signOut()
                val sharedPref = SharedPreferenceHelper(context)
                sharedPref.saveStringData("accessToken", "")
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

    fun updateUserProfile(user: OrderUiStateUser, context: Context) {
        viewModelScope.launch {
            try {
                _uiState.value = UserState.Loading
                val token = getToken(context)
                if (token.isNullOrEmpty()) {
                    _uiState.value = UserState.Error("Token não encontrado.")
                    return@launch
                }

                val userId = supabase.auth.retrieveUser(token).id

                // Atualizar no banco de dados
                val response = supabase
                    .from("users")
                    .update(
                        mapOf(
                            "name" to user.name,
                            "surname" to user.surname,
                            "sex" to user.sex
                        )
                    ){
                        filter {
                            eq("idAuth", userId)
                        }
                    }

                _uiState.value = UserState.Success("Perfil atualizado com sucesso.")
            } catch (e: Exception) {
                _uiState.value = UserState.Error("Erro ao atualizar perfil: ${e.message ?: "Erro desconhecido"}")
            }
        }
    }

}