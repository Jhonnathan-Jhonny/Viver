package com.project.viver.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.project.viver.R
import com.project.viver.data.models.SingleButton
import kotlin.random.Random

fun getRandomMotivationalPhrase(): String {
    val motivationalPhrases = listOf(
        "Sua saúde é seu maior patrimônio. Cuide bem dela!",
        "Pequenas mudanças hoje criam grandes resultados amanhã.",
        "Alimente seu corpo com comida saudável e sua mente com pensamentos positivos.",
        "Exercite-se não apenas para mudar seu corpo, mas para fortalecer sua mente.",
        "O progresso é mais importante do que a perfeição.",
        "Cada passo que você dá para cuidar de si mesmo é um passo na direção certa.",
        "A energia que você coloca em sua saúde retorna em forma de felicidade.",
        "Você não precisa ser extremo, apenas consistente.",
        "Uma vida saudável é construída com escolhas saudáveis, dia após dia.",
        "Mexa-se! O movimento é a chave para uma mente e corpo equilibrados.",
        "Escolha alimentos que nutram seu corpo e alma.",
        "A disciplina hoje será o seu orgulho amanhã.",
        "Ser saudável não é um objetivo, é um estilo de vida.",
        "Cada treino te deixa mais perto de uma versão melhor de você mesmo.",
        "Hidrate-se e veja como sua energia pode mudar!",
        "O que você come hoje, você será amanhã.",
        "Uma caminhada de 30 minutos por dia pode transformar sua vida.",
        "Faça de você sua maior prioridade.",
        "Você é mais forte do que pensa. Prove isso para si mesmo!",
        "Nada é mais valioso do que acordar com saúde e disposição.",
        "Transforme seus hábitos e transforme sua vida.",
        "Descanso também é parte de uma vida saudável. Cuide do seu sono!",
        "Cada gota de suor é um passo mais perto do seu objetivo.",
        "Nutrição e exercício são a receita para uma vida longa e feliz.",
        "Seja gentil com seu corpo, ele é sua casa para a vida toda."
    )
    return motivationalPhrases[Random.nextInt(motivationalPhrases.size)]
}


@Composable
fun HomeScreen(
    onNewListButtonClicked: () -> Unit,
    onListsButtonClicked: () -> Unit,
) {

    val isLoading by remember { mutableStateOf(false) }
    val randomMotivationalPhrases = getRandomMotivationalPhrase()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(colorResource(id = R.color.First))
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .clip(
                    RoundedCornerShape(
                        topStart = 32.dp,
                        topEnd = 32.dp
                    )
                ) // Arredondamento nos cantos superiores
                .background(Color.White) // Cor da tela principal
                .padding(16.dp), // Padding interno, opcional
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = androidx.compose.ui.Alignment.CenterHorizontally
        ) {

            Image(
                painter = painterResource(id = R.drawable.colorful_logo),
                contentDescription = "Logo do aplicativo",
                modifier = Modifier
                    .size(300.dp)
            )

            Spacer(modifier = Modifier.height(50.dp))

            Text(
                text = randomMotivationalPhrases,
                color = colorResource(id = R.color.on_secondary_container_dark),
                fontSize = 16.sp,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(80.dp))


            SingleButton(
                onClick = { onNewListButtonClicked() },
                isLoading = isLoading,
                buttonName = stringResource(R.string.nova_lista),
                colorButton = colorResource(id = R.color.First),
                colorText = Color.White
            )

            Spacer(modifier = Modifier.height(16.dp))

            SingleButton(
                onClick = { onListsButtonClicked() },
                isLoading = isLoading,
                buttonName = "Minhas listas",
                colorButton = colorResource(id = R.color.First),
                colorText = Color.White
            )

            Spacer(modifier = Modifier.height(200.dp))
        }
    }
}

@Preview(showBackground = true)
@Composable
fun HomeScreenPreview() {
    HomeScreen(
        onNewListButtonClicked = { /*TODO*/ }) {
    }
}