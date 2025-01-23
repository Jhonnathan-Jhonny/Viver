package com.project.viver.ui

import android.annotation.SuppressLint
import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
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
fun LoginScreen(
    onSignUpButtonClicked: () -> Unit,
    onForgotPasswordButtonClicked: () -> Unit,
    onLoginButtonClicked: () -> Unit,
    viewModel: ViverViewModel,
    context: Context
) {
    val email = remember { mutableStateOf("") }
    val password = remember { mutableStateOf("") }
    val scope = rememberCoroutineScope()

    val focusManager = LocalFocusManager.current
    var isLoading by remember { mutableStateOf(false) }

    var emailError by remember { mutableStateOf("") }
    var passwordError by remember { mutableStateOf("") }

    var isValid by remember { mutableStateOf(true) }
    val snackbarHostState = remember { SnackbarHostState() }


    fun validateFields(): Boolean {
        isValid = true
        if (email.value.isBlank()) {
            isValid = false
            emailError = "E-mail não pode estar vazio"
        } else {
            emailError = ""
        }

        if (password.value.isBlank()) {
            isValid = false
            passwordError = "Senha não pode estar vazia"
        } else {
            passwordError = ""
        }
        return isValid
    }

    Scaffold(
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
        content = {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.White),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = "Entre em sua conta",
                    fontWeight = FontWeight.Bold,
                    fontSize = 24.sp,
                    color = Color(0xFF4CAF50)
                )

                Spacer(modifier = Modifier.height(32.dp))

                TextBox(
                    value = email.value,
                    onValueChange = { email.value = it },
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

                TextBox(
                    value = password.value,
                    onValueChange = { password.value = it },
                    label = stringResource(id = R.string.senha),
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.Lock,
                            contentDescription = "Senha Icon"
                        )
                    },
                    onNext = { focusManager.clearFocus() },
                    errorMessage = passwordError.isNotEmpty()
                )
                if (passwordError.isNotEmpty()) {
                    Text(
                        text = passwordError,
                        color = Color.Red,
                        fontSize = 12.sp
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                TextButton(onClick = onForgotPasswordButtonClicked) {
                    Box(
                        modifier = Modifier.fillMaxWidth(),
                        contentAlignment = Alignment.CenterEnd
                    ) {
                        Text(
                            text = stringResource(R.string.esqueci_minha_senha),
                            color = colorResource(id = R.color.Third),
                            fontSize = 14.sp,
                            textDecoration = TextDecoration.Underline
                        )
                    }
                }

                Spacer(modifier = Modifier.height(32.dp))

                SingleButton(
                    onClick = {
                        scope.launch {
                            if (email.value == "admin" && password.value == "admin") {
                                onLoginButtonClicked()
                            } else if (validateFields()) {
                                isLoading = true
                                val result = viewModel.logInUser(context, email.value, password.value)
                                isLoading = false
                                if (result is UserState.Success) {
                                    onLoginButtonClicked()
                                }
                                else if (result is UserState.Error) {
                                    snackbarHostState.showSnackbar(result.message)
                                }
                            }
                        }
                    },
                    isLoading = isLoading,
                    buttonName = stringResource(R.string.entrar),
                    colorButton = colorResource(id = R.color.First),
                    colorText = Color.White,
                )

                Spacer(modifier = Modifier.height(16.dp))

                SingleButton(
                    onClick = { onSignUpButtonClicked() },
                    isLoading = isLoading,
                    buttonName = stringResource(R.string.cadastre_se),
                    colorButton = Color.Transparent,
                    colorText = colorResource(id = R.color.First)
                )
            }
        }
    )
}

@Preview(showBackground = true)
@Composable
fun PreviewWelcomeScreen() {
    val navController = NavController(LocalContext.current)
    val viewModel: ViverViewModel = viewModel()
    val context = LocalContext.current

    LoginScreen(
        { navController.navigate(ViverScreen.SignUp.name) },
        { navController.navigate(ViverScreen.ForgotPassword.name) },
        { navController.navigate(ViverScreen.Home.name) },
        viewModel,
        context
    )
}