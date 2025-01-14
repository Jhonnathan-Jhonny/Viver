package com.project.viver.ui

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
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

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun ForgotPasswordScreen(
    emailContact: String,
    onOkButtonClicked: () -> Unit
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
                            text = "Redefinir senha",
                            color = colorResource(id = R.color.First),
                            fontWeight = FontWeight.Bold,
                            fontSize = 18.sp
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        Text(
                            text = "Entre em contato com",
                            color = Color.Gray,
                            fontSize = 14.sp
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        Text(
                            text = emailContact,
                            color = Color.Black,
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp,
                            modifier = Modifier.padding(vertical = 50.dp)
                        )

                        Spacer(modifier = Modifier.height(24.dp))

                        SingleButton(
                            onClick = onOkButtonClicked,
                            isLoading = false,
                            buttonName = "OK",
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
        emailContact = "william.henry.moody@my-own-personal-domain.com",
        onOkButtonClicked = {navController.navigate(ViverScreen.Login.name)}
    )
}



