package com.project.viver

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
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


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ViverAppTopBar(
    currentScreen: ViverScreen, //Serve para config. o botão da tela anterior
    canNavigateBack: Boolean,
    navigateUp: () -> Unit,
    modifier: Modifier = Modifier
){
    TopAppBar(
        title = { Text(stringResource(R.string.app_name))}
    )
}
@Composable
fun ViverApp(
    navController: NavHostController = rememberNavController()
) {
    //Serve para criar o botão voltar para a tela anterior
    //Se houver uma tela atrás da tela atual na backstack, o botão "Up" vai aparecer.
    // Você pode usar uma expressão booleana para identificar se o botão "Up" será mostrado.
    val backStackEntry by navController.currentBackStackEntryAsState()
    val currentScreen = ViverScreen.valueOf(
        (backStackEntry?.destination?.route ?: ViverScreen.StartOrder).toString()
    )
    Scaffold(
        topBar = {
            ViverAppTopBar(
                currentScreen = currentScreen,
                //verificando se a propriedade previousBackStackEntry de navController não é igual a nulo.
                canNavigateBack = navController.previousBackStackEntry != null,
                navigateUp = {navController.navigateUp()} //Para voltar à tela anterior
            )
        }
    ) { innerPadding ->
        // Definir o destino inicial como StartOrder
        NavHost(
            navController = navController,
            startDestination = ViverScreen.StartOrder.name,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(route = ViverScreen.StartOrder.name) {
                StartOrderScreen(
                    navController = navController
                )
            }
            composable(route = ViverScreen.Login.name) {
                LoginScreen()
            }
//            composable(route = ViverScreen.SignUp.name) {
//                SignUpScreen(navController = navController)
//            }
//            composable(route = ViverScreen.Home.name) {
//                HomeScreen(navController = navController)
//            }
//            composable(route = ViverScreen.Profile.name) {
//                ProfileScreen(navController = navController)
//            }
//            composable(route = ViverScreen.ForgotPassword.name) {
//                ForgotPasswordScreen(navController = navController)
//            }
//            composable(route = ViverScreen.ValidateEmail.name) {
//                ValidateEmailScreen(navController = navController)
//            }
//            composable(route = ViverScreen.Restrictions.name) {
//                RestrictionsScreen(navController = navController)
//            }
//            composable(route = ViverScreen.NewPassword.name) {
//                NewPasswordScreen(navController = navController)
//            }
//            composable(route = ViverScreen.NewList.name) {
//                NewListScreen(navController = navController)
//            }
//            composable(route = ViverScreen.EmailSent.name) {
//                EmailSentScreen(navController = navController)
//            }
//            composable(route = ViverScreen.EditedPasswordSuccessfully.name) {
//                EditedPasswordSuccessfullyScreen(navController = navController)
//            }
//            composable(route = ViverScreen.ConfirmPassword.name) {
//                ConfirmPasswordScreen(navController = navController)
//            }
//            composable(route = ViverScreen.EspecificList.name) {
//                EspecificListScreen(navController = navController)
//            }
//            composable(route = ViverScreen.Lists.name) {
//                ListsScreen(navController = navController)
//            }
        }
    }
}