package com.project.viver.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.project.viver.R
import com.project.viver.ViverScreen
import com.project.viver.models.OrderUiStateUser

@Composable
fun SignUpScreen(
    onSignUpButtonClicked: () -> Unit,
    onBackLoginButtonClicked: () -> Unit,
    viewModel: ViverViewModel
) {
    var name by remember { mutableStateOf("") }
    var surname by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var passwordConfirm by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }
    var passwordConfirmVisible by remember { mutableStateOf(false) }
    var selectedSex by remember { mutableStateOf("M") }

    // Variáveis de erro
    var nameError by remember { mutableStateOf("") }
    var emailError by remember { mutableStateOf("") }
    var passwordError by remember { mutableStateOf("") }
    var passwordConfirmError by remember { mutableStateOf("") }

    fun isValidEmail(email: String): Boolean =
        Regex("^[^@\\s]+@[^@\\s]+\\.[^@\\s]+\$").matches(email)

    fun isValidPassword(password: String): Boolean =
        password.length >= 6 &&
                password.any { it.isUpperCase() } &&
                password.any { !it.isLetterOrDigit() }

    fun validateFields(): Boolean {
        var isValid = true

        if (name.isBlank()) {
            nameError = "O nome não pode ser vazio"
            isValid = false
        } else {
            nameError = ""
        }

        if (!isValidEmail(email)) {
            emailError = "O email deve ser válido (exemplo@dominio.com)"
            isValid = false
        } else {
            emailError = ""
        }

        if (!isValidPassword(password)) {
            passwordError = "A senha deve ter pelo menos 6 caracteres, uma letra maiúscula e um caractere especial"
            isValid = false
        } else {
            passwordError = ""
        }

        if (password != passwordConfirm) {
            passwordConfirmError = "As senhas não correspondem"
            isValid = false
        } else {
            passwordConfirmError = ""
        }

        return isValid
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Faça seu cadastro",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold,
            color = colorResource(id = R.color.First),
            modifier = Modifier.padding(bottom = 12.dp)
        )

        // Nome
        TextBox(
            value = name,
            onValueChange = { name = it },
            label = "Nome",
            leadingIcon = { Icon(painter = painterResource(id = R.drawable.person_icon), contentDescription = null) }
        )
        if (nameError.isNotBlank()) {
            Text(nameError, color = Color.Red, style = MaterialTheme.typography.bodySmall)
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Sobrenome
        TextBox(
            value = surname,
            onValueChange = { surname = it },
            label = "Sobrenome",
            leadingIcon = { Icon(painter = painterResource(id = R.drawable.person_icon), contentDescription = null) }
        )

        Spacer(modifier = Modifier.height(8.dp))

        // E-mail
        TextBox(
            value = email,
            onValueChange = { email = it },
            label = "E-mail*",
            leadingIcon = { Icon(Icons.Default.Email, contentDescription = null) }
        )
        if (emailError.isNotBlank()) {
            Text(emailError, color = Color.Red, style = MaterialTheme.typography.bodySmall)
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Senha
        TextBox(
            value = password,
            onValueChange = { password = it },
            label = "Senha",
            leadingIcon = { Icon(Icons.Default.Lock, contentDescription = null) },
            trailingIcon = {
                Icon(
                    imageVector = if (passwordVisible) Icons.Default.VisibilityOff else Icons.Default.Visibility,
                    contentDescription = null,
                    modifier = Modifier.clickable {
                        passwordVisible = !passwordVisible
                    }
                )
            },
            visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation()
        )
        if (passwordError.isNotBlank()) {
            Text(passwordError, color = Color.Red, style = MaterialTheme.typography.bodySmall)
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Confirmar Senha
        TextBox(
            value = passwordConfirm,
            onValueChange = { passwordConfirm = it },
            label = "Confirmar Senha",
            leadingIcon = { Icon(Icons.Default.Lock, contentDescription = null) },
            trailingIcon = {
                Icon(
                    imageVector = if (passwordConfirmVisible) Icons.Default.VisibilityOff else Icons.Default.Visibility,
                    contentDescription = null,
                    modifier = Modifier.clickable {
                        passwordConfirmVisible = !passwordConfirmVisible
                    }
                )
            },
            visualTransformation = if (passwordConfirmVisible) VisualTransformation.None else PasswordVisualTransformation()
        )
        if (passwordConfirmError.isNotBlank()) {
            Text(passwordConfirmError, color = Color.Red, style = MaterialTheme.typography.bodySmall)
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Sexo
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            val color = colorResource(id = R.color.Third)

            Text(
                text = "Sexo",
                color = colorResource(id = R.color.First),
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier.padding(end = 8.dp)
            )

            RadioButton(
                selected = selectedSex == "M",
                onClick = { selectedSex = "M" },
                colors = RadioButtonDefaults.colors(
                    selectedColor = color,
                    unselectedColor = color
                )
            )
            Text(
                text = "M",
                color = color,
                style = MaterialTheme.typography.bodyMedium
            )

            Spacer(modifier = Modifier.width(16.dp))

            RadioButton(
                selected = selectedSex == "F",
                onClick = { selectedSex = "F" },
                colors = RadioButtonDefaults.colors(
                    selectedColor = color,
                    unselectedColor = color
                )
            )
            Text(
                text = "F",
                color = color,
                style = MaterialTheme.typography.bodyMedium
            )

            Spacer(modifier = Modifier.width(16.dp))

            RadioButton(
                selected = selectedSex == "Outro",
                onClick = { selectedSex = "Outro" },
                colors = RadioButtonDefaults.colors(
                    selectedColor = color,
                    unselectedColor = color
                )
            )
            Text(
                text = "Outro",
                color = color,
                style = MaterialTheme.typography.bodyMedium
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Botão de cadastro
        Button(
            onClick = {
                if (validateFields()) {
                    viewModel.saveUserToSupabase(
                        OrderUiStateUser(
                            name = name,
                            surname = surname,
                            email = email,
                            password = password,
                            sex = selectedSex
                        )
                    )
                    onSignUpButtonClicked()
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
                .height(50.dp)
                .border(1.dp, color = colorResource(id = R.color.First), shape = RoundedCornerShape(8.dp)),
            colors = ButtonDefaults.buttonColors(Color.Transparent)
        ) {
            Text(
                text = "Cadastrar",
                color = colorResource(id = R.color.First),
                fontSize = 18.sp
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        TextButton(onClick = onBackLoginButtonClicked) {
            Text(
                text = "Já possui uma conta? Entre",
                color = colorResource(id = R.color.First),
                fontSize = 15.sp
            )
        }
    }
}



@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TextBox(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    leadingIcon: @Composable (() -> Unit)? = null,
    trailingIcon: @Composable (() -> Unit)? = null,
    visualTransformation: VisualTransformation = VisualTransformation.None
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = {
            Text(
                text = label,
                color = colorResource(id = R.color.Third),
                style = MaterialTheme.typography.bodyLarge
            )
        },
        leadingIcon = { leadingIcon?.invoke() },
        trailingIcon = { trailingIcon?.invoke() },
        visualTransformation = visualTransformation,
        shape = RoundedCornerShape(16.dp),
        colors = TextFieldDefaults.outlinedTextFieldColors(
            cursorColor = colorResource(id = R.color.Third),
            focusedBorderColor = colorResource(id = R.color.Third),
            unfocusedBorderColor = colorResource(id = R.color.Third),
        ),
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
    )
}

@Preview(showBackground = true)
@Composable
fun SignUpScreenPreview() {
    val viewModel: ViverViewModel = viewModel()
    SignUpScreen(
        onSignUpButtonClicked = { ViverScreen.ValidateEmail },
        onBackLoginButtonClicked = { ViverScreen.Login },
        viewModel = viewModel
    )
}
