package com.project.viver.ui

import android.annotation.SuppressLint
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.project.viver.R
import com.project.viver.ViverScreen
import com.project.viver.ViverViewModel
import com.project.viver.data.models.UserState
import kotlinx.coroutines.delay

@SuppressLint("StateFlowValueCalledInComposition")
@Composable
fun StartOrderScreen(
    navController: NavHostController,
    viewModel: ViverViewModel
) {
    // Atraso de 4 segundos para exibir a tela de boas-vindas
    LaunchedEffect(key1 = true) {
        delay(4000) // Espera por 4 segundos
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(80.dp))

        // Logo
        Image(
            painter = painterResource(id = R.drawable.colorful_logo),
            contentDescription = "App Logo",
            modifier = Modifier
                .size(width = 205.dp, height = 203.dp),
            alignment = Alignment.Center
        )

        Spacer(modifier = Modifier.height(20.dp))

        // Title
        Text(
            text = "Bem vindo!",
            fontWeight = FontWeight.Bold,
            fontSize = 24.sp,
            color = colorResource(id = R.color.First),
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Subtitle
        Text(
            text = stringResource(R.string.descubra_refei_es_saud_veis_personalizadas_para_o_seu_estilo_de_vida),
            fontSize = 16.sp,
            color = colorResource(id = R.color.First),
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.weight(1f))

        // Bottom wave-like shape
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(300.dp)
        ) {
            Canvas(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(300.dp)
            ) {
                val waveHeight = 80f
                val waveWidth = size.width

                val path = androidx.compose.ui.graphics.Path().apply {
                    moveTo(0f, waveHeight)
                    cubicTo(
                        waveWidth / 4, -waveHeight,
                        3 * waveWidth / 4, 5 * waveHeight,
                        waveWidth, waveHeight
                    )
                    lineTo(waveWidth, size.height)
                    lineTo(0f, size.height)
                    close()
                }

                drawPath(path = path, color = Color(0xFF68B684))
            }

            // Adiciona o texto no centro
            Text(
                text = "Vamos lá!",
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp,
                color = Color.White,
                modifier = Modifier.align(Alignment.Center),
                textAlign = TextAlign.Center
            )
        }
    }

    // Navegação para tela seguinte, com base no estado do token
    LaunchedEffect(key1 = viewModel.uiState.value) {
        when (val state = viewModel.uiState.value) {
            is UserState.Loading -> {
                // O loading pode ser tratado se necessário
            }
            is UserState.Error -> {
                val errorMessage = (state as? UserState.Error)?.message ?: "Unknown error"
                navController.navigate(ViverScreen.Login.name) {
                    popUpTo(ViverScreen.StartOrder.name) { inclusive = true }
                }
            }
            is UserState.Success -> {
                delay(4000) // Espera mais 4 segundos para garantir tempo suficiente
                navController.navigate(ViverScreen.Login.name) {
                    popUpTo(ViverScreen.StartOrder.name) { inclusive = true }
                }
            }
        }
    }

    // Adicionando um atraso fixo para garantir que a tela de boas-vindas seja visível
    LaunchedEffect(key1 = true) {
        delay(4000) // Isso garante que a tela será visível por 4 segundos
    }
}



@Preview(showBackground = true)
@Composable
fun StartOrderScreenPreview() {
    val navController = rememberNavController()
    val viewModel = ViverViewModel()
    StartOrderScreen(
        navController = navController,
        viewModel
    )
}
