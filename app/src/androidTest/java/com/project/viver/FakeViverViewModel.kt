package com.project.viver

import android.content.Context
import com.project.viver.data.models.OrderUiStateUser
import com.project.viver.data.models.UserState

class FakeViverViewModel : ViverViewModel() {

    // Variável para simular resultados de cadastro
    var signUpUserResult: UserState = UserState.Success("")

    // Variável para simular resultados de login
    var loginUserResult: UserState = UserState.Success("")

    override suspend fun signUpUser(
        context: Context,
        user: OrderUiStateUser
    ): UserState {
        return signUpUserResult
    }
}
