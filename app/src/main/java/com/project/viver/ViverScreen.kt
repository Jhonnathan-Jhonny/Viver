package com.project.viver

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.project.viver.ui.LoginScreen
import com.project.viver.ui.StartOrderScreen

enum class ViverScreen {
    ConfirmPassword,
    EditedPasswordSuccessfully,
    EmailSent,
    EspecificList,
    ForgotPassword,
    Home,
    Lists,
    Login,
    NewList,
    NewPassword,
    Profile,
    Restrictions,
    SignUp,
    StartOrder,
    ValidateEmail
}


@Composable
fun ViverAppTopBar1(
    currentScreen: ViverScreen,
    modifier: Modifier = Modifier
) {
    // Verifica se a tela atual é uma das que precisam do topo personalizado
    val shouldShowTopBar = currentScreen in listOf(
        ViverScreen.Login,
        ViverScreen.ForgotPassword,
        ViverScreen.EmailSent,
        ViverScreen.SignUp,
        ViverScreen.ValidateEmail,
        ViverScreen.EditedPasswordSuccessfully,
        ViverScreen.ConfirmPassword
    )

    if (shouldShowTopBar) {
        Box(
            modifier = modifier
                .fillMaxWidth()
                .height(300.dp)
        ) {
            Canvas(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(300.dp)
                    .background(Color.White)
            ) {
                val waveHeight = 80f // Altura da onda
                val waveWidth = size.width // Largura total do Canvas

                val path = androidx.compose.ui.graphics.Path().apply {
                    // Começa no canto inferior esquerdo
                    moveTo(0f, size.height - waveHeight)
                    // Cria a curva arredondada para cima
                    cubicTo(
                        waveWidth / 4, size.height + waveHeight, // Primeiro ponto de controle (abaixo do eixo X)
                        3 * waveWidth / 4, size.height - 5 * waveHeight, // Segundo ponto de controle (acima do eixo X)
                        waveWidth, size.height - waveHeight // Ponto final (no canto inferior direito)
                    )
                    // Continua desenhando os lados e a parte superior do Box
                    lineTo(waveWidth, 0f)
                    lineTo(0f, 0f)
                    close()
                }

                // Desenha o fundo com a onda invertida
                drawPath(
                    path = path,
                    brush = Brush.verticalGradient(
                        colors = listOf(
                            Color(0xFF68B684), // Verde no fundo
                            Color(0xFFFFFFFF)  // Branco no topo
                        ),
                        startY = size.height, // Começa com o verde na parte inferior
                        endY = -800f            // Termina com o branco na parte superior
                    )
                )
            }

            // Adiciona a estela (star) acima do ícone e um pouco à direita
            Icon(
                painter = painterResource(id = R.drawable.star_trajectory),
                contentDescription = stringResource(R.string.star_trajectory),
                modifier = Modifier
                    .offset(x = 130.dp, y = 40.dp) // Desloca a estrela para a direita e para cima
                    .size(width = 174.dp, height = 60.dp),
                tint = Color.White
            )

            // Adiciona o ícone ou título no centro do Box
            Icon(
                painter = painterResource(id = R.drawable.colorful_logo), // Substitua pelo ícone do seu app
                contentDescription = stringResource(R.string.logo),
                modifier = Modifier
                    .align(Alignment.Center)
                    .size(width = 249.dp, height = 248.dp),
                tint = Color.White
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ViverAppTopBar2(
    currentScreen: ViverScreen,
    modifier: Modifier = Modifier
) {
    val shouldShowTopBar = currentScreen == ViverScreen.Home

    val shouldShowTopBarWithoutProfileIcon = currentScreen in listOf(
        ViverScreen.Home,
        ViverScreen.Profile,
        ViverScreen.Restrictions,
        ViverScreen.NewList,
        ViverScreen.Lists,
        ViverScreen.EspecificList,
    )
    if (shouldShowTopBarWithoutProfileIcon) {
        TopAppBar(
            colors = TopAppBarDefaults.mediumTopAppBarColors(
                containerColor = Color.Transparent
            ),
            modifier = Modifier
                .background(
                    brush = Brush.verticalGradient(
                        0f to Color(0xFFA8D5BA),
                        1f to Color(0xFF68B684),
                    )
                ),
            title = {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween, // Isso coloca os botões nas extremidades
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Botão da esquerda
                    Button(
                        onClick = { /* TODO: Ação ao clicar */ },
                        modifier = Modifier
                            .size(width = 50.dp, height = 50.dp),
                        shape = CircleShape,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0x40111111),
                            contentColor = Color.White
                        ),
                        contentPadding = PaddingValues(0.dp), // Remove padding do botão
                        elevation = ButtonDefaults.buttonElevation(0.dp),
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.back),
                            contentDescription = stringResource(R.string.voltar_para_tela_anterior),
                            modifier = Modifier.size(30.dp)
                        )
                    }

                    // Botão da direita
                    if (shouldShowTopBar) {
                        Button(
                            onClick = { /* TODO: Ação ao clicar */ },
                            modifier = Modifier
                                .size(width = 66.dp, height = 51.dp)
                                .padding(end = 16.dp), // Ajuste a distância do lado direito
                            shape = CircleShape,
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color(0x40111111),
                                contentColor = Color.White
                            ),
                            contentPadding = PaddingValues(0.dp), // Remove padding do botão
                            elevation = ButtonDefaults.buttonElevation(0.dp),
                        ) {
                            Icon(
                                painter = painterResource(id = R.drawable.person_icon),
                                contentDescription = stringResource(R.string.icone_para_ir_para_perfil),
                                modifier = Modifier.size(30.dp)
                            )
                        }
                    }
                }
            }
        )
    }
}



@Composable
fun ViverApp(
    navController: NavHostController = rememberNavController()
) {
    val backStackEntry by navController.currentBackStackEntryAsState()
    val currentScreen = ViverScreen.valueOf(
        (backStackEntry?.destination?.route ?: ViverScreen.StartOrder).toString()
    )
    Scaffold(
        topBar = {}, // Remova o TopAppBar padrão
        content = { innerPadding ->
            Column(modifier = Modifier.padding(innerPadding)) {
                // Topo customizado
                ViverAppTopBar1(currentScreen = currentScreen)
                ViverAppTopBar2(currentScreen = currentScreen)

                // Conteúdo principal
                NavHost(
                    navController = navController,
                    startDestination = ViverScreen.StartOrder.name,
                    modifier = Modifier
                ) {
                    composable(route = ViverScreen.StartOrder.name) {
                        StartOrderScreen(
                            navController = navController
                        )
                    }
                    composable(route = ViverScreen.Login.name) {
                        LoginScreen()
                    }
//                    composable(route = ViverScreen.SignUp.name) {
//                        SignUpScreen(navController = navController)
//                    }
//                    composable(route = ViverScreen.Home.name) {
//                        HomeScreen(navController = navController)
//                    }
//                    composable(route = ViverScreen.Profile.name) {
//                        ProfileScreen(navController = navController)
//                    }
//                    composable(route = ViverScreen.ForgotPassword.name) {
//                        ForgotPasswordScreen(navController = navController)
//                    }
//                    composable(route = ViverScreen.ValidateEmail.name) {
//                        ValidateEmailScreen(navController = navController)
//                    }
//                    composable(route = ViverScreen.Restrictions.name) {
//                        RestrictionsScreen(navController = navController)
//                    }
//                    composable(route = ViverScreen.NewPassword.name) {
//                        NewPasswordScreen(navController = navController)
//                    }
//                    composable(route = ViverScreen.NewList.name) {
//                        NewListScreen(navController = navController)
//                    }
//                    composable(route = ViverScreen.EmailSent.name) {
//                        EmailSentScreen(navController = navController)
//                    }
//                    composable(route = ViverScreen.EditedPasswordSuccessfully.name) {
//                        EditedPasswordSuccessfullyScreen(navController = navController)
//                    }
//                    composable(route = ViverScreen.ConfirmPassword.name) {
//                        ConfirmPasswordScreen(navController = navController)
//                    }
//                    composable(route = ViverScreen.EspecificList.name) {
//                        EspecificListScreen(navController = navController)
//                    }
//                    composable(route = ViverScreen.Lists.name) {
//                        ListsScreen(navController = navController)
//                    }
                }
            }
        }
    )
}

@Preview(showBackground = true)
@Composable
fun ViverAppPreview() {
    ViverApp()
}