package com.project.viver.ui

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.project.viver.models.OrderUiStateUser
import com.project.viver.supabase
import io.github.jan.supabase.postgrest.from
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ViverViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(OrderUiStateUser())
    val uiState: StateFlow<OrderUiStateUser> = _uiState.asStateFlow()

    fun saveUserToSupabase(user: OrderUiStateUser) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val response = supabase
                    .from("users")
                    .insert(
                        mapOf(
                            "name" to user.name,
                            "surname" to user.surname,
                            "email" to user.email,
                            "password" to user.password,
                            "sex" to user.sex
                        )
                    )

                val decodedResponse = response.decodeAs<OrderUiStateUser>()
                Log.d("SupabaseResponse", "Decoded Response: $decodedResponse")
            } catch (e: Exception) {
                Log.e("SupabaseError", "Error during insert or deserialization: ${e.message}")
            }
        }
    }

}