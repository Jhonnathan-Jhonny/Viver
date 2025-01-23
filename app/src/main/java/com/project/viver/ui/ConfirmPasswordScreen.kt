package com.project.viver.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
    val uiState by viewModel.uiState.collectAsState()
    val userProfile by viewModel.userProfile.observeAsState()

    // Controle de loading agora vem do ViewModel
    val isLoading = uiState is UserState.Loading
    val errorMessage = (uiState as? UserState.Error)?.message

    // Armazenar erro de validação de senha vazia
    val passwordError = remember { mutableStateOf("") }

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
        passwordError.value?.let {
            Text(
                text = it,
                color = Color.Red,
                fontSize = 14.sp,
                modifier = Modifier
                    .padding(top = 8.dp)
            )
        }

        DoubleButton(
            onClickCancel = { onCancelButtonClicked() },
            onClickConfirm = {
                // Verifica se o campo de senha está vazio
                if (confirmPassword.value.isBlank()) {
                    // Se estiver vazio, exibe um erro
                    passwordError.value = "A senha não pode ser vazia."
                } else {
                    passwordError.value = "" // Limpa a mensagem de erro
                    val email = userProfile?.email ?: ""
                    viewModel.confirmPassword(email, confirmPassword.value)
                    if (uiState is UserState.Success) {
                        onConfirmButtonClicked()
                    }
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