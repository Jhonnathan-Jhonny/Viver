package com.project.viver.ui

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Card
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.project.viver.R
import com.project.viver.ViverScreen
import com.project.viver.data.models.SingleButton
import com.project.viver.data.models.TextBox

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun ForgotPasswordScreen(
    onSendButtonClicked: () -> Unit
) {
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

                        Spacer(modifier = Modifier.height(24.dp))

                        // Configurar essa função e gerenciar telas
                        TextBox(
                            value = "",
                            label = "Email",
                            onValueChange = {}
                        )

                        Spacer(modifier = Modifier.height(48.dp))

                        SingleButton(
                            onClick = onSendButtonClicked,
                            isLoading = false,
                            buttonName = "Enviar",
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
    ForgotPasswordScreen(
        onSendButtonClicked = {navController.navigate(ViverScreen.Login.name)}
    )
}



