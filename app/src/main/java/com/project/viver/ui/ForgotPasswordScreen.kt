package com.project.viver.ui

import android.annotation.SuppressLint
import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.project.viver.R
import com.project.viver.ViverScreen
import com.project.viver.ViverViewModel
import com.project.viver.data.models.SingleButton
import com.project.viver.data.models.TextBox
import com.project.viver.data.models.UserState
import kotlinx.coroutines.launch

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun ForgotPasswordScreen(
    onSendButtonClicked: () -> Unit,
    viewModel: ViverViewModel,
    context: Context
) {
    val confirmEmail = remember {mutableStateOf("")}
    var isLoading by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()
    var isValid by remember { mutableStateOf(true) }
    var emailError by remember { mutableStateOf("") }
    val focusManager = LocalFocusManager.current

    fun validateFields(): Boolean {
        isValid = true
        if (confirmEmail.value.isBlank()) {
            isValid = false
            emailError = "E-mail não pode estar vazio"
        } else {
            emailError = ""
        }
        return isValid
    }

    Scaffold(
        content = {
            Box(
                modifier = Modifier
                    .fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Card(
                    modifier = Modifier
                        .fillMaxSize(),
                    shape = RectangleShape
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(Color.White),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Text(
                            text = "Esqueceu a senha?",
                            color = colorResource(id = R.color.First),
                            fontWeight = FontWeight.Bold,
                            fontSize = 18.sp
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        Text(
                            text = "Enviaremos um e-mail com a nova senha de acesso. \n Após login recomendamos alterar sua senha!",
                            color = Color.Gray,
                            fontSize = 14.sp
                        )

                        Spacer(modifier = Modifier.height(32.dp))

                        TextBox(
                            value = confirmEmail.value,
                            onValueChange = { confirmEmail.value = it },
                            label = stringResource(id = R.string.e_mail),
                            leadingIcon = {
                                Icon(
                                    imageVector = Icons.Default.Email,
                                    contentDescription = "E-mail Icon"
                                )
                            },
                            onNext = { focusManager.moveFocus(FocusDirection.Down) },
                            errorMessage = emailError.isNotEmpty()
                        )
                        if (emailError.isNotEmpty()) {
                            Text(
                                text = emailError,
                                color = Color.Red,
                                fontSize = 12.sp
                            )
                        }

                        Spacer(modifier = Modifier.height(32.dp))

                        SingleButton(
                            onClick = {
                                scope.launch {
                                    if (validateFields()) {
                                        isLoading = true
                                        val result = viewModel.ForgotPassword(confirmEmail.value)
                                        isLoading = false
                                        if (result is UserState.Success) {
                                            Toast.makeText(
                                                context,
                                                "Logado com sucesso",
                                                Toast.LENGTH_LONG
                                            ).show()
                                            onSendButtonClicked()
                                        } else if (result is UserState.Error) {
                                            Toast.makeText(
                                                context,
                                                "Usuário inexistente ou email ou senha incorreta",
                                                Toast.LENGTH_LONG
                                            ).show()
                                        }
                                    }
                                }
                            },
                            isLoading = isLoading,
                            buttonName = stringResource(R.string.enviar),
                            colorButton = colorResource(id = R.color.First),
                            colorText = Color.White
                        )
                    }
                }
            }
        }
    )
}


@Preview(showBackground = true)
@Composable
fun PreviewForgotPasswordScreen() {
    val navController = NavController(LocalContext.current)
    val viewModel: ViverViewModel = viewModel()
    val context = LocalContext.current


    ForgotPasswordScreen(
        onSendButtonClicked = {navController.navigate(ViverScreen.Login.name)},
        viewModel = viewModel,
        context = context
    )
}



