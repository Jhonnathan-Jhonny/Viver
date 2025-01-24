package com.project.viver.ui

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
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.project.viver.R
import com.project.viver.ViverViewModel
import com.project.viver.data.models.DoubleButton
import com.project.viver.data.models.TextBox
import com.project.viver.data.models.UserState

@Composable
fun ConfirmPasswordScreen(
    onConfirmButtonClicked: () -> Unit,
    onCancelButtonClicked: () -> Unit,
    viewModel: ViverViewModel
) {
    val confirmPassword = remember { mutableStateOf("") }
    val uiState by viewModel.uiState.collectAsState(UserState.Loading)
    val userProfile by viewModel.userProfile.observeAsState()

    // Controle de loading agora vem do ViewModel
    val isLoading = uiState is UserState.Loading
    val errorMessage = (uiState as? UserState.Error)?.message

    // Armazenar erro de validação de senha vazia
    val passwordError = remember { mutableStateOf("") }

    // Verificar se a autenticação foi bem-sucedida e chamar a ação correspondente
    LaunchedEffect(uiState) {
        if (uiState is UserState.Success) {
            // Verifica explicitamente se a mensagem de sucesso é esperada
            val successMessage = (uiState as UserState.Success).message
            if (successMessage == "Senha confirmada.") {
                viewModel.userProfile.value!!.password = confirmPassword.value
                onConfirmButtonClicked()
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White),
        verticalArrangement = Arrangement.SpaceAround,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = stringResource(R.string.confirme_sua_senha),
            fontSize = 24.sp,
            color = Color.Black
        )

        TextBox(
            value = confirmPassword.value,
            onValueChange = { confirmPassword.value = it },
            label = stringResource(id = R.string.senha),
        )

        // Exibir a mensagem de erro caso exista
        errorMessage?.let {
            Text(
                text = it,
                color = Color.Red,
                fontSize = 14.sp,
                modifier = Modifier
                    .padding(top = 8.dp)
            )
        }

        // Exibir erro caso a senha esteja vazia
        if (passwordError.value.isNotBlank()) {
            Text(
                text = passwordError.value,
                color = Color.Red,
                fontSize = 14.sp,
                modifier = Modifier
                    .padding(top = 8.dp)
            )
        }

        DoubleButton(
            onClickCancel = { onCancelButtonClicked() },
            onClickConfirm = {
                if (confirmPassword.value.isBlank()) {
                    passwordError.value = "A senha não pode ser vazia."
                } else {
                    passwordError.value = ""
                    val email = userProfile?.email ?: ""
                    viewModel.confirmPassword(email, confirmPassword.value)
                }
            },
            isLoading = isLoading
        )
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewConfirmPasswordScreen() {
    val viewModel = ViverViewModel()
    val onConfirmButtonClicked = {}
    val onCancelButtonClicked = {}

    ConfirmPasswordScreen(
        onConfirmButtonClicked = onConfirmButtonClicked,
        onCancelButtonClicked = onCancelButtonClicked,
        viewModel = viewModel
    )
}