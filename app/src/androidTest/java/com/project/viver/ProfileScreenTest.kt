package com.project.viver

import android.content.Context
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onAllNodesWithTag
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.project.viver.data.models.OrderUiStateUser
import com.project.viver.data.models.UserState
import com.project.viver.ui.ProfileScreen
import kotlinx.coroutines.flow.MutableStateFlow
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class ProfileScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    private lateinit var mockViewModel: ViverViewModel

    @Before
    fun setUp() {
        mockViewModel = object : ViverViewModel() {
            override fun setUiState(newState: UserState) {
                super.setUiState(newState) // Chama a função original para modificar _uiState
            }

            // Sobrescreva outros métodos que você precisa simular
            override val userProfile: LiveData<OrderUiStateUser> = MutableLiveData(
                OrderUiStateUser(name = "Nome", surname = "Sobrenome", sex = "M", email = "teste@email.com")
            )
            override val uiState: MutableStateFlow<UserState> = MutableStateFlow(UserState.Success(""))

            override fun fetchUserProfile(context: Context) { /* Mock */ }
            override fun updateUserProfile(user: OrderUiStateUser, context: Context) { /* Mock */ }
            override suspend fun deleteUser(context: Context) { /* Mock */ }
            override fun logOutUser(context: Context) { /* Mock */ }
        }
    }

    @Test
    fun testProfileScreen_deleteConfirmationDialog() {
        composeTestRule.setContent {
            ProfileScreen(
                viewModel = mockViewModel,
                context = InstrumentationRegistry.getInstrumentation().context,
                onEditPasswordButtonClicked = {},
                onDeleteActionConfirmedButtonClicked = {}
            )
        }

        composeTestRule.onNodeWithText("Desativar conta").performClick()
        composeTestRule.onNodeWithText("Confirmação").assertIsDisplayed()
        composeTestRule.onNodeWithText("Realmente deseja apagar permanentemente sua conta?").assertIsDisplayed()
        composeTestRule.onNodeWithText("Não").performClick()
        composeTestRule.onNodeWithText("Desativar conta").assertIsDisplayed()

        composeTestRule.onNodeWithText("Desativar conta").performClick()
        composeTestRule.onNodeWithText("Sim").performClick()
    }

    @Test
    fun testProfileScreenErrorState() {
        // Define o estado de erro no ViewModel
        val viewModel = ViverViewModel()
        viewModel._uiState.value = UserState.Error("Erro ao carregar perfil")

        composeTestRule.setContent {
            ProfileScreen(
                viewModel = viewModel,
                context = InstrumentationRegistry.getInstrumentation().targetContext,
                onEditPasswordButtonClicked = {},
                onDeleteActionConfirmedButtonClicked = {}
            )
        }

        // Verifica se a mensagem de erro está visível
        composeTestRule.onNodeWithTag("errorMessage").assertIsDisplayed()
    }

    @Test
    fun testProfileScreen_loadingState() {
        mockViewModel = object : ViverViewModel() {
            override val uiState: MutableStateFlow<UserState> = MutableStateFlow(UserState.Loading)
            override val userProfile: LiveData<OrderUiStateUser> = MutableLiveData()
        }

        composeTestRule.setContent {
            ProfileScreen(
                viewModel = mockViewModel,
                context = InstrumentationRegistry.getInstrumentation().targetContext,
                onEditPasswordButtonClicked = {},
                onDeleteActionConfirmedButtonClicked = {}
            )
        }

        composeTestRule.onNodeWithTag("loadingIndicator").assertIsDisplayed()
    }

    @Test
    fun testProfileScreen_confirmDeleteAccount() {
        var deleteConfirmed = false

        composeTestRule.setContent {
            ProfileScreen(
                viewModel = mockViewModel,
                context = InstrumentationRegistry.getInstrumentation().context,
                onEditPasswordButtonClicked = {},
                onDeleteActionConfirmedButtonClicked = { deleteConfirmed = true }
            )
        }

        composeTestRule.onNodeWithText("Desativar conta").performClick()
        composeTestRule.onNodeWithText("Sim").performClick()

        assert(deleteConfirmed)
    }

    @Test
    fun testProfileFieldsDisplayCorrectly() {
        val context = ApplicationProvider.getApplicationContext<Context>()

        composeTestRule.setContent {
            ProfileScreen(
                viewModel = mockViewModel,
                context = context,
                onEditPasswordButtonClicked = {},
                onDeleteActionConfirmedButtonClicked = {}
            )
        }

        // Verificar se os campos de nome e sobrenome estão visíveis
        composeTestRule.onAllNodesWithTag("nameField")[0].assertExists()
        composeTestRule.onAllNodesWithTag("surnameField")[0].assertExists()

        // Verificar se o botão de editar perfil está visível
        composeTestRule.onNodeWithTag("editProfileButton").assertExists()
    }
}