package com.project.viver.ui

import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.project.viver.R
import com.project.viver.ViverViewModel
import com.project.viver.data.models.DoubleButton
import com.project.viver.data.models.TextBox
import com.project.viver.data.models.UserState
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

@OptIn(DelicateCoroutinesApi::class)
@Composable
fun NewPasswordScreen(
    onConfirmButtonClicked: () -> Unit,
    onCancelButtonClicked: () -> Unit,
    context: Context,
    viewModel: ViverViewModel,
    previousPassword: String
) {
    val newPassword = remember { mutableStateOf("") }
    val passwordError = remember { mutableStateOf("") }
    val uiState by viewModel.uiState.collectAsState(UserState.Loading)
    val isLoading = uiState is UserState.Loading

    // Monitorar estado para verificar sucesso e navegar
    LaunchedEffect(uiState) {
        if (uiState is UserState.Success) {
            // Verifica explicitamente se a mensagem de sucesso é esperada
            val successMessage = (uiState as UserState.Success).message
            if (successMessage == "Senha atualizada com sucesso.") {
                // Realiza o logout e navega para a tela de login
                viewModel.logOutUser(context)
                onConfirmButtonClicked() // Navega para a tela de login
            }
        }
    }

    // Função de validação de senha
    fun isValidPassword(password: String): Boolean {
        return password.length >= 6 &&
                password.any { it.isUpperCase() } &&
                password.any { !it.isLetterOrDigit() }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White),
        verticalArrangement = Arrangement.SpaceAround,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = stringResource(R.string.nova_senha),
            fontSize = 24.sp,
            color = Color.Black
        )

        TextBox(
            value = newPassword.value,
            onValueChange = { newPassword.value = it },
            label = stringResource(id = R.string.senha)
        )

        if (passwordError.value.isNotBlank()) {
            Text(
                text = passwordError.value,
                color = Color.Red,
                fontSize = 14.sp,
                modifier = Modifier.padding(top = 8.dp)
            )
        }

        DoubleButton(
            onClickCancel = { onCancelButtonClicked() },
            onClickConfirm = {
                // Verificar se a senha não está vazia e se é diferente da senha anterior
                when {
                    newPassword.value.isBlank() -> {
                        passwordError.value = "A senha não pode estar vazia."
                    }
                    newPassword.value == previousPassword -> {
                        passwordError.value = "A nova senha não pode ser a mesma que a anterior."
                    }
                    !isValidPassword(newPassword.value) -> {
                        passwordError.value = "A senha deve ter pelo menos 6 caracteres, uma letra maiúscula e um caractere especial."
                    }
                    else -> {
                        passwordError.value = ""
                        GlobalScope.launch {
                            viewModel.updatePassword(context, newPassword.value)
                        }
                    }
                }
            },
            isLoading = isLoading
        )
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewNewPasswordScreen() {
    val onConfirmButtonClicked = {}
    val onCancelButtonClicked = {}
    NewPasswordScreen(
        onConfirmButtonClicked = onConfirmButtonClicked,
        onCancelButtonClicked = onCancelButtonClicked,
        viewModel = ViverViewModel(),
        context = LocalContext.current, previousPassword = ""
    )
}

