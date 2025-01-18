package com.project.viver

import android.content.Context
import com.project.viver.data.models.OrderUiStateUser
import com.project.viver.data.models.UserState

class FakeViverViewModel : ViverViewModel() {

    // Variável para simular resultados de cadastro
    var signUpUserResult: UserState = UserState.Success("")

    // Variável para simular resultados de login
    var loginUserResult: UserState = UserState.Success("")

    // Variável para armazenar mensagens de erro simuladas
    private var errorMessage: String? = null

    override suspend fun signUpUser(
        context: Context,
        user: OrderUiStateUser
    ): UserState {
        return signUpUserResult
    }

    // Método para simular login de usuários
    fun loginUser(
        context: Context,
        email: String,
        password: String
    ): UserState {
        return loginUserResult
    }

    // Métodos para manipular o estado de erro
    fun setErrorState(message: String) {
        errorMessage = message
    }

    fun getErrorState(): String? {
        return errorMessage
    }
}
