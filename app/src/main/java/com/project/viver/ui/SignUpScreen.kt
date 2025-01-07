package com.project.viver.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Visibility
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.project.viver.R
import com.project.viver.ViverScreen

@Composable
fun SignUpScreen(
    onSignUpButtonClicked: () -> Unit,
    onBackLoginButtonClicked: () -> Unit
) {
    // Variáveis de estado
    var nome by remember { mutableStateOf("") }
    var sobrenome by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var senha by remember { mutableStateOf("") }
    var confirmarSenha by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        // Título
        Text(
            text = "Faça seu cadastro",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold,
            color = colorResource(id = R.color.First),
            modifier = Modifier.padding(bottom = 12.dp)
        )

        // Nome
        TextBox(
            value = nome,
            onValueChange = { nome = it },
            label = "Nome",
            leadingIcon = { Icon(painter = painterResource(id = R.drawable.person_icon), contentDescription = null) }
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Sobrenome
        TextBox(
            value = sobrenome,
            onValueChange = { sobrenome = it },
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

        Spacer(modifier = Modifier.height(8.dp))

        // Senha
        TextBox(
            value = senha,
            onValueChange = { senha = it },
            label = "Senha",
            leadingIcon = { Icon(Icons.Default.Lock, contentDescription = null) },
            trailingIcon = { Icon(Icons.Default.Visibility, contentDescription = null) },
            visualTransformation = PasswordVisualTransformation()
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Confirmar Senha
        TextBox(
            value = confirmarSenha,
            onValueChange = { confirmarSenha = it },
            label = "Confirmar Senha",
            leadingIcon = { Icon(Icons.Default.Lock, contentDescription = null) },
            trailingIcon = { Icon(Icons.Default.Visibility, contentDescription = null) },
            visualTransformation = PasswordVisualTransformation()
        )

        Spacer(modifier = Modifier.height(16.dp))

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
                selected = false,
                onClick = {},
                colors = RadioButtonDefaults.colors(
                    selectedColor = color,
                    unselectedColor = color
                ),
                modifier = Modifier.padding(end = 4.dp)
            )
            Text(
                text = "M",
                color = color,
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.padding(end = 8.dp)
            )

            RadioButton(
                selected = false,
                onClick = {},
                colors = RadioButtonDefaults.colors(
                    selectedColor = color,
                    unselectedColor = color
                ),
                modifier = Modifier.padding(end = 4.dp)
            )
            Text(
                text = "F",
                color = color,
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.padding(end = 8.dp)
            )

            RadioButton(
                selected = false,
                onClick = {},
                colors = RadioButtonDefaults.colors(
                    selectedColor = color,
                    unselectedColor = color
                ),
                modifier = Modifier.padding(end = 4.dp)
            )
            Text(
                text = "Outro",
                color = color,
                style = MaterialTheme.typography.bodyMedium
            )
        }


        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = onSignUpButtonClicked,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
                .size(height = 50.dp, width = 150.dp)
                .border(1.dp, color = colorResource(id = R.color.First), shape = RoundedCornerShape(8.dp)), // Definindo a borda
            colors = ButtonDefaults.buttonColors(Color.Transparent)
        ) {
            Text(
                text = "Cadastrar",
                color = colorResource(id = R.color.First),
                fontSize = 18.sp
            )
        }


        // Botão para voltar ao login
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
    visualTransformation: PasswordVisualTransformation = PasswordVisualTransformation()
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(label) },
        leadingIcon = { leadingIcon?.invoke() },
        trailingIcon = { trailingIcon?.invoke() },
        visualTransformation = visualTransformation,
        shape = RoundedCornerShape(16.dp),
        colors = TextFieldDefaults.outlinedTextFieldColors(
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
    SignUpScreen(
        onSignUpButtonClicked = { ViverScreen.ValidateEmail },
        onBackLoginButtonClicked = { ViverScreen.Login }
    )
}
