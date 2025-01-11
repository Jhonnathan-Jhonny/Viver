package com.project.viver.ui

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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.project.viver.R
import com.project.viver.ViverScreen
import kotlinx.coroutines.delay

@Composable
fun StartOrderScreen(navController: NavHostController) {
    var hasNavigated by remember { mutableStateOf(false) }

    // Navega para a tela de Login após o delay, apenas uma vez
    LaunchedEffect(key1 = hasNavigated) {
        if (!hasNavigated) {
            delay(4000) // Aguarda 4 segundos
            navController.navigate(ViverScreen.Login.name) {
                popUpTo(ViverScreen.StartOrder.name) { inclusive = true }
            }
            hasNavigated = true
        }
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
            painter = painterResource(id = R.drawable.colorful_logo), // Replace with your logo resource
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
            text = "Descubra refeições saudáveis\npersonalizadas para o seu estilo de vida.",
            fontSize = 16.sp,
            color =  colorResource(id = R.color.First),
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.weight(1f))

        // Bottom wave-like shape (or green background)
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
                val waveHeight = 80f // Maior altura para a onda
                val waveWidth = size.width // Largura total do Canvas

                val path = androidx.compose.ui.graphics.Path().apply {
                    // Começa no canto superior esquerdo
                    moveTo(0f, waveHeight)
                    // Cria a curva arredondada mais acentuada
                    cubicTo(
                        waveWidth / 4, -waveHeight, // Primeiro ponto de controle (acima do eixo X)
                        3 * waveWidth / 4, 5 * waveHeight, // Segundo ponto de controle (abaixo do eixo X)
                        waveWidth, waveHeight // Ponto final (no canto superior direito)
                    )
                    // Continua desenhando os lados e a parte inferior do Box
                    lineTo(waveWidth, size.height)
                    lineTo(0f, size.height)
                    close()
                }

                // Desenha o fundo com a onda mais curvada
                drawPath(path = path, color = Color(0xFF68B684))  // Cor diretamente em hexadecimal
            }

            // Adiciona o texto no centro do Box
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
}

@Preview(showBackground = true)
@Composable
fun StartOrderScreenPreview() {
    val navController = rememberNavController()
    StartOrderScreen(navController = navController)
}
