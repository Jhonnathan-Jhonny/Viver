package com.project.viver.data.models

sealed class UserState {
    data object Loading : UserState()
    data class Success(val message: String): UserState()
    data class Error(val message: String): UserState()
}