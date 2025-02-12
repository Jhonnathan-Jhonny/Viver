package com.project.viver

import androidx.activity.ComponentActivity
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onAllNodesWithText
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import androidx.navigation.compose.ComposeNavigator
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.testing.TestNavHostController
import com.project.viver.data.models.UserState
import com.project.viver.ui.LoginScreen
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class LoginScreenTest {

    @get:Rule
    val composeTestRule = createAndroidComposeRule<ComponentActivity>()

    private lateinit var navController: TestNavHostController
    private lateinit var fakeViewModel: FakeViverViewModel

    @Before
    fun setup() {
        navController = TestNavHostController(composeTestRule.activity).apply {
            navigatorProvider.addNavigator(ComposeNavigator())
        }

        // Inicialize o fakeViewModel aqui
        fakeViewModel = FakeViverViewModel()

        composeTestRule.setContent {
            // Configura o gráfico de navegação manualmente, sem precisar de R.navigation.nav_graph
            NavHost(
                navController = navController,
                startDestination = "login_screen" // Defina a rota inicial
            ) {
                composable("login_screen") {
                    LoginScreen(
                        onSignUpButtonClicked = { navController.navigate("sign_up") },
                        onForgotPasswordButtonClicked = { navController.navigate("forgot_password") },
                        onLoginButtonClicked = { navController.navigate("home") },
                        viewModel = fakeViewModel,
                        context = composeTestRule.activity // Obtém o contexto da activity
                    )
                }
                composable("sign_up") {
                    // Defina a navegação para a tela de cadastro
                }
                composable("forgot_password") {
                    // Defina a navegação para a tela de esqueci a senha
                }
                composable("home") {
                    // Defina a navegação para a tela inicial
                }
            }
        }
    }

    @Test
    fun validateEmptyFields() {
        composeTestRule.onNodeWithText("Entrar").performClick()
        composeTestRule.onNodeWithText("E-mail não pode estar vazio").assertExists()
        composeTestRule.onNodeWithText("Senha não pode estar vazia").assertExists()
    }

    @Test
    fun clickForgotPasswordButton() {
        composeTestRule.onNodeWithText("Esqueci minha senha").performClick()
        assertEquals("forgot_password", navController.currentDestination?.route)
    }

    @Test
    fun clickSignUpButton() {
        composeTestRule.onNodeWithText("Cadastre-se").performClick()
        assertEquals("sign_up", navController.currentDestination?.route)
    }

    @Test
    fun loginWithCorrectCredentials() {
        composeTestRule.onNodeWithContentDescription("E-mail Icon").performTextInput("admin")
        composeTestRule.onNodeWithContentDescription("Senha Icon").performTextInput("admin")
        composeTestRule.onNodeWithText("Entrar").performClick()

        assertEquals("home", navController.currentDestination?.route)
    }

    @Test
    fun snackbarDisplaysErrorMessage() {
        fakeViewModel.loginUserResult = UserState.Error("Usuário inexistente ou email ou senha incorreta")

        composeTestRule.onNodeWithContentDescription("E-mail Icon").performTextInput("email@exemplo.com")
        composeTestRule.onNodeWithContentDescription("Senha Icon").performTextInput("senha123")

        composeTestRule.onNodeWithText("Entrar").performClick()

        composeTestRule.waitUntil(timeoutMillis = 5000) {
            composeTestRule.onAllNodesWithText("Usuário inexistente ou email ou senha incorreta").fetchSemanticsNodes().isNotEmpty()
        }

        composeTestRule.onNodeWithText("Usuário inexistente ou email ou senha incorreta").assertIsDisplayed()
    }

    @Test
    fun loginWithCorrectCredentialsAndWaitForLoading() {
        composeTestRule.onNodeWithContentDescription("E-mail Icon").performTextInput("admin")
        composeTestRule.onNodeWithContentDescription("Senha Icon").performTextInput("admin")
        composeTestRule.onNodeWithText("Entrar").performClick()

        // Esperar até o indicador de carregamento desaparecer
        composeTestRule.waitUntil(timeoutMillis = 5000) {
            composeTestRule.onNodeWithTag("progress_indicator").assertDoesNotExist()
            true  // Retorna verdadeiro quando o indicador desapareceu
        }

        // Verificar se a navegação foi realizada após o carregamento
        assertEquals("home", navController.currentDestination?.route)
    }
}
