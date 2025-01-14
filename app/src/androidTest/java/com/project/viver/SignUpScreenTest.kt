package com.project.viver

import androidx.activity.ComponentActivity
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.test.assertCountEquals
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onAllNodesWithContentDescription
import androidx.compose.ui.test.onFirst
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import androidx.navigation.compose.ComposeNavigator
import androidx.navigation.testing.TestNavHostController
import com.project.viver.data.models.UserState
import com.project.viver.ui.SignUpScreen
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class SignUpScreenTest {

    @get:Rule
    val composeTestRule = createAndroidComposeRule<ComponentActivity>()
    private lateinit var navController: TestNavHostController
    private lateinit var fakeViewModel: FakeViverViewModel

    // Inicializar a tela antes de cada teste
    @Before
    fun setup() {
        fakeViewModel = FakeViverViewModel()
        composeTestRule.setContent {
            navController = TestNavHostController(LocalContext.current).apply {
                navigatorProvider.addNavigator(ComposeNavigator())
            }
            SignUpScreen(
                onSignUpButtonClicked = { /* TODO: Navegação após sucesso */ },
                onBackLoginButtonClicked = { /* TODO: Navegação para login */ },
                viewModel = fakeViewModel,
                context = LocalContext.current
            )
        }
    }

    @Test
    fun verifyInitialScreenState() {
        composeTestRule.onNodeWithText("Faça seu cadastro").assertExists()
        composeTestRule.onNodeWithText("Nome").assertExists()
        composeTestRule.onNodeWithText("Sobrenome").assertExists()
        composeTestRule.onNodeWithText("E-mail*").assertExists()  // Modificado para "E-mail*"
        composeTestRule.onNodeWithText("Senha").assertExists()
        composeTestRule.onNodeWithText("Confirmar Senha").assertExists()  // Modificado para "Confirmar Senha"
        composeTestRule.onNodeWithText("Cadastrar").assertExists()
    }

    @Test
    fun validateEmptyFields() {
        composeTestRule.onNodeWithText("Cadastrar").performClick()
        composeTestRule.onNodeWithText("O nome não pode ser vazio").assertExists()
        composeTestRule.onNodeWithText("O email deve ser válido (exemplo@dominio.com)").assertExists()
        composeTestRule.onNodeWithText("A senha deve ter pelo menos 6 caracteres, uma letra maiúscula e um caractere especial").assertExists()
    }

    @Test
    fun validateEmailFormat() {
        composeTestRule.onNodeWithText("E-mail*").performTextInput("invalid email")
        composeTestRule.onNodeWithText("Cadastrar").performClick()
        composeTestRule.onNodeWithText("O email deve ser válido (exemplo@dominio.com)").assertExists()
    }

    @Test
    fun validatePasswordMismatch() {
        composeTestRule.onNodeWithText("Senha").performTextInput("Valid@123")
        composeTestRule.onNodeWithText("Confirmar Senha").performTextInput("Mismatch@123") // Certifique-se de que o texto está correto
        composeTestRule.onNodeWithText("Cadastrar").performClick()
        composeTestRule.onNodeWithText("As senhas não correspondem").assertExists()
    }


    @Test
    fun validateSuccessfulSignUp() {
        composeTestRule.onNodeWithText("Nome").performTextInput("Test User")
        composeTestRule.onNodeWithText("E-mail*").performTextInput("testuser@example.com")
        composeTestRule.onNodeWithText("Senha").performTextInput("Valid@123")
        composeTestRule.onNodeWithText("Confirmar Senha").performTextInput("Valid@123")
        composeTestRule.onNodeWithText("Cadastrar").performClick()
    }

    @Test
    fun validateSexSelection() {
        composeTestRule.onNodeWithText("M").performClick()
        composeTestRule.onNodeWithText("F").performClick()
        composeTestRule.onNodeWithText("Outro").performClick()
        // Verifique a seleção final (Opcional)
    }

    @Test
    fun validatePasswordVisibilityToggle() {
        // Encontre todos os ícones com a descrição "Toggle Password Visibility" e clique no primeiro
        composeTestRule.onAllNodesWithContentDescription("Toggle Password Visibility")
            .assertCountEquals(5)  // Verifique que temos exatamente 5 nós com a descrição esperada
            .onFirst()  // Pega o primeiro nó encontrado
            .performClick()  // Realiza o clique no ícone encontrado

        // Agora verifique o estado da senha após o clique (esperamos que a senha esteja visível)
        composeTestRule.onNodeWithText("Senha")
            .assertExists()  // Verifica se o campo de senha está presente

        // Verifique o estado do ícone novamente, após o clique
        composeTestRule.onAllNodesWithContentDescription("Toggle Password Visibility")
            .onFirst()  // Novamente pegamos o primeiro ícone
            .assertExists()  // Confirma que o ícone de visibilidade ainda está presente
    }

    @Test
    fun signUpScreen_whenSignUpFails_displaysErrorMessage() {
        // Simulando falha no cadastro
        fakeViewModel.signUpUserResult = UserState.Error("Email já cadastrado")

        // Simular entrada de dados
        composeTestRule.onNodeWithText("Nome").performTextInput("Test Name")
        composeTestRule.onNodeWithText("E-mail*").performTextInput("existing@example.com")
        composeTestRule.onNodeWithText("Senha").performTextInput("Password1!")
        composeTestRule.onNodeWithText("Confirmar Senha").performTextInput("Password1!")

        // Clicar no botão de cadastro
        composeTestRule.onNodeWithText("Cadastrar").performClick()

        // Verificar mensagem de erro
        composeTestRule.onNodeWithText("Email já cadastrado").assertIsDisplayed()
    }

}
