package com.project.viver.ui

import androidx.lifecycle.ViewModel
import com.project.viver.models.OrderUiStateUser
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow


class OrderViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(OrderUiStateUser())
    val uiState: StateFlow<OrderUiStateUser> = _uiState.asStateFlow()

    fun setBasicUserInfo(name: String, surname: String, email: String, password: String, sex: String) {
        _uiState.value = _uiState.value.copy(name = name, surname = surname, email = email, password = password, sex = sex)
    }
}