@file:OptIn(InternalAPI::class)

package com.project.viver
import android.content.Context
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateMapOf
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.project.viver.data.models.Food
import com.project.viver.data.models.MealPlan
import com.project.viver.data.models.OrderUiStateUser
import com.project.viver.data.models.UserState
import com.project.viver.data.network.SupabaseClient.supabase
import com.project.viver.utils.SharedPreferenceHelper
import io.github.jan.supabase.exceptions.RestException
import io.github.jan.supabase.gotrue.auth
import io.github.jan.supabase.gotrue.providers.builtin.Email
import io.github.jan.supabase.postgrest.from
import io.github.jan.supabase.postgrest.query.Columns
import io.ktor.util.InternalAPI
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch


open class ViverViewModel : ViewModel() {

    private val _uiState = MutableStateFlow<UserState>(UserState.Loading)
    val uiState: StateFlow<UserState> = _uiState

    private val _userProfile = MutableLiveData<OrderUiStateUser>()
    val userProfile: LiveData<OrderUiStateUser> = _userProfile

    private val _mealPlan = MutableStateFlow<MealPlan?>(null)
    val mealPlan: StateFlow<MealPlan?> = _mealPlan

    private fun saveToken(context: Context) {
        viewModelScope.launch {
            val accessToken = supabase.auth.currentAccessTokenOrNull()
            val sharedPref = SharedPreferenceHelper(context)
            sharedPref.saveStringData("accessToken",accessToken)
        }
    }

    private fun getToken(context: Context): String? {
        val sharedPref = SharedPreferenceHelper(context)
        return sharedPref.getStringData("accessToken")
    }

    fun fetchUserProfile(context: Context) {
        viewModelScope.launch {
            _uiState.value = UserState.Loading
            try {
                val token = getToken(context)
                if (token.isNullOrEmpty()) {
                    _uiState.value = UserState.Error("Token não encontrado.")
                    return@launch
                }

                // Recupera o usuário autenticado e seu email
                val user = supabase.auth.retrieveUser(token)
                val email = user.email

                if (email == null) {
                    _uiState.value = UserState.Error("Email não encontrado.")
                    return@launch
                }

                // Obtém o userId
                val userId = user.id

                // Consulta o perfil do usuário na tabela 'users'
                val response = supabase
                    .from("users")
                    .select(columns = Columns.list("name, surname, sex, weight, physical_activity_level")) {
                        filter {
                            eq("idAuth", userId)
                        }
                    }
                    .decodeSingleOrNull<OrderUiStateUser>()

                // Adiciona o email e atualiza o userProfile
                if (response != null) {
                    _userProfile.value = response.copy(email = email)
                }

                _uiState.value = UserState.Success("Perfil do usuário carregado com sucesso.")
            } catch (e: Exception) {
                _uiState.value =
                    UserState.Error("Erro ao buscar perfil: ${e.message ?: "Erro desconhecido"}")
            }
        }
    }


    open suspend fun signUpUser(context: Context, user: OrderUiStateUser): UserState {
        return try {
            // Registrar o usuário
            val response = supabase.auth.signUpWith(Email) {
                email = user.email
                password = user.password
            }

            // Recuperar o ID do usuário
            val userId = response?.id ?: supabase.auth.currentSessionOrNull()?.user?.id

            if (userId == null) {
                _uiState.value = UserState.Error("Failed to retrieve user ID after signup")
                throw Exception("Failed to retrieve user ID after signup")
            }

            // Inserir no banco de dados
            supabase
                .from("users")
                .insert(
                    mapOf(
                        "idAuth" to userId,
                        "name" to user.name,
                        "surname" to user.surname,
                        "sex" to user.sex
                    )
                )

            _uiState.value = UserState.Success("User registered successfully")
            saveToken(context)
            UserState.Success("User registered successfully")
        } catch (e: Exception) {
            _uiState.value = UserState.Error("Error during registration: ${e.message}")
            UserState.Error("Error during registration: ${e.message}")
        }
    }

    fun checkIfUserLoggedIn(context: Context): Boolean {
        val token = getToken(context)
        return !token.isNullOrEmpty() && !token.startsWith("eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9")
    }


    suspend fun logInUser(context: Context, email: String, password: String): UserState {
        return try {
            supabase.auth.signInWith(Email) {
                this.email = email
                this.password = password
            }
            _uiState.value = UserState.Success("Logged in user successfully")
            saveToken(context)
            UserState.Success("Logged in user successfully")
        } catch (e: Exception) {
            _uiState.value = UserState.Error("Error during login: ${e.message}")
            UserState.Error("Usuário inexistente ou email ou senha incorreta")
        }
    }

    fun logOutUser(context: Context) {
        viewModelScope.launch {
            try {
                val sharedPref = SharedPreferenceHelper(context)
                sharedPref.saveStringData("accessToken", null)
                _uiState.value = UserState.Success("Logged out user successfully")
            } catch (e: Exception) {
                _uiState.value = UserState.Error("Error during logout: ${e.message}")
            }
        }
    }

    fun isUserLoggedIn(
        context: Context,
    ) {
        viewModelScope.launch {
            try {
                _uiState.value = UserState.Loading
                val token = getToken(context)
                if(token.isNullOrEmpty()) {
                    _uiState.value = UserState.Success("User not logged in!")
                } else {
                    supabase.auth.retrieveUser(token)
                    supabase.auth.refreshCurrentSession()
                    saveToken(context)
                    _uiState.value = UserState.Success("User already logged in!")
                }
            } catch (e: RestException) {
                _uiState.value = UserState.Error(e.error)
            }
        }
    }

    fun updateUserProfile(user: OrderUiStateUser, context: Context) {
        viewModelScope.launch {
            try {
                _uiState.value = UserState.Loading
                val token = getToken(context)
                if (token.isNullOrEmpty()) {
                    _uiState.value = UserState.Error("Token não encontrado.")
                    return@launch
                }

                val userId = supabase.auth.retrieveUser(token).id

                // Atualizar no banco de dados
                supabase
                    .from("users")
                    .update(
                        mapOf(
                            "name" to user.name,
                            "surname" to user.surname,
                            "sex" to user.sex
                        )
                    ){
                        filter {
                            eq("idAuth", userId)
                        }
                    }

                _uiState.value = UserState.Success("Perfil atualizado com sucesso.")
            } catch (e: Exception) {
                _uiState.value = UserState.Error("Erro ao atualizar perfil: ${e.message ?: "Erro desconhecido"}")
            }
        }
    }

    fun confirmPassword(email: String, currentPassword: String) {
        viewModelScope.launch {
            _uiState.value = UserState.Loading
            try {
                // Tenta fazer login com email e senha
                supabase.auth.signInWith(Email) {
                    this.email = email
                    this.password = currentPassword
                }
                _uiState.value = UserState.Success("Senha confirmada.")
            } catch (e: Exception) {
                // Caso ocorra algum erro durante o processo de autenticação
                _uiState.value = UserState.Error("Senha icorreta")
            }
        }
    }

    suspend fun updatePassword(context: Context, newPassword: String) {
        // Importando o token da chave de serviço
        supabase.auth.importAuthToken("eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJzdXBhYmFzZSIsInJlZiI6ImZ5aW9lZHNqcHdrcnFwaXluYXhjIiwicm9sZSI6InNlcnZpY2Vfcm9sZSIsImlhdCI6MTczNTgyNTU5OSwiZXhwIjoyMDUxNDAxNTk5fQ.zjr5ShFjvXXPx2QpSfjcfXF73oNC_h5Gy400GfQ6_zw")

        // Recuperando o token da sessão
        val token = getToken(context)
        if (token.isNullOrEmpty()) {
            _uiState.value = UserState.Error("Token não encontrado.")
            return
        }

        // Recuperando o usuário autenticado e seu email
        val user = supabase.auth.retrieveUser(token)
        if (user.id.isEmpty()) {
            _uiState.value = UserState.Error("ID do usuário não encontrado.")
            return
        }
        try {
            supabase.auth.admin.updateUserById(user.id) {
                this.password = newPassword
            }
            _uiState.value = UserState.Success("Senha atualizada com sucesso.")
        } catch (e: Exception) {
            _uiState.value = UserState.Error("Erro ao atualizar a senha: ${e.message ?: "Erro desconhecido"}")
        }
    }

    suspend fun deleteUser(context: Context) {
        supabase.auth.importAuthToken("eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJzdXBhYmFzZSIsInJlZiI6ImZ5aW9lZHNqcHdrcnFwaXluYXhjIiwicm9sZSI6InNlcnZpY2Vfcm9sZSIsImlhdCI6MTczNTgyNTU5OSwiZXhwIjoyMDUxNDAxNTk5fQ.zjr5ShFjvXXPx2QpSfjcfXF73oNC_h5Gy400GfQ6_zw")

        // Obtendo o token do contexto
        val token = getToken(context)
        if (token.isNullOrEmpty()) {
            _uiState.value = UserState.Error("Token de autenticação não encontrado.")
            return
        }

        try {
            // Recuperando o usuário autenticado
            val user = supabase.auth.retrieveUser(token)

            // Deletando o usuário pelo ID
            supabase.auth.admin.deleteUser(user.id)
            supabase
                .from("users")
                .delete{
                    filter {
                        eq("idAuth", user.id)
                    }
                }

            // Atualizando o estado para sucesso
            _uiState.value = UserState.Success("Usuário deletado com sucesso.")
        } catch (e: Exception) {
            // Atualizando o estado para erro com mensagem detalhada
            _uiState.value = UserState.Error("Erro ao deletar o usuário: ${e.message ?: "Erro desconhecido"}")
        }
    }

    fun updateUserInfoForNewList(user: OrderUiStateUser, context: Context) {
        viewModelScope.launch {
            try {
                _uiState.value = UserState.Loading
                val token = getToken(context)
                if (token.isNullOrEmpty()) {
                    _uiState.value = UserState.Error("Token não encontrado.")
                    return@launch
                }

                val userId = supabase.auth.retrieveUser(token).id

                // Atualizar no banco de dados
                supabase
                    .from("users")
                    .update(
                        mapOf(
                            "weight" to user.weight,
                            "physical_activity_level" to user.physical_activity_level
                        )
                    ) {
                        filter {
                            eq("idAuth", userId)
                        }
                    }

                _uiState.value = UserState.Success("Peso e nível de atividade atualizados com sucesso.")
            } catch (e: Exception) {
                _uiState.value = UserState.Error("Erro ao atualizar Peso e nível de atividade: ${e.message ?: "Erro desconhecido"}")
            }
        }
    }

    fun resetUserState() {
        _uiState.value = UserState.Loading
    }

    private val foodDatabase = listOf(
        // Proteínas
        Food(name = "Frango grelhado", calories = 165, protein = 31.0, fat = 3.6, carbs = 0.0),
        Food(name = "Peixe assado", calories = 206, protein = 22.0, fat = 12.0, carbs = 0.0),
        Food(name = "Ovo cozido", calories = 155, protein = 13.0, fat = 11.0, carbs = 1.1),
        // Gorduras
        Food(name = "Abacate", calories = 160, protein = 2.0, fat = 15.0, carbs = 9.0),
        Food(name = "Azeite de oliva", calories = 884, protein = 0.0, fat = 100.0, carbs = 0.0),
        Food(name = "Castanhas", calories = 607, protein = 15.0, fat = 55.0, carbs = 20.0),
        // Carboidratos
        Food(name = "Arroz integral", calories = 112, protein = 2.6, fat = 0.9, carbs = 23.0),
        Food(name = "Batata-doce", calories = 86, protein = 1.6, fat = 0.1, carbs = 20.0),
        Food(name = "Quinoa", calories = 120, protein = 4.1, fat = 1.9, carbs = 21.3)
    )

    val meal = mutableStateListOf<List<String>>()  // Alterando para uma lista de listas
    val totals = mutableStateMapOf<String, Double>()

    // Função para gerar o plano alimentar
    fun generateMealPlan(weight: Double, activityLevel: Double, calorieReduction: Int = 500) {
        // 1. Calcular calorias diárias
        val dailyCalories = (weight * 22 * activityLevel) - calorieReduction

        // 2. Calcular macronutrientes
        val proteinGrams = weight * 2 // 2g de proteína por kg
        val fatGrams = weight * 0.8 // Média de 0.8g de gordura por kg
        val proteinCalories = proteinGrams * 4
        val fatCalories = fatGrams * 9
        val carbCalories = dailyCalories - (proteinCalories + fatCalories)
        val carbGrams = carbCalories / 4

        // 3. Criar refeições baseadas nos alimentos disponíveis
        val morningSnack = createMeal(proteinGrams / 4, fatGrams / 4, carbGrams / 4)
        val lunch = createMeal(proteinGrams / 4, fatGrams / 4, carbGrams / 4)
        val afternoonSnack = createMeal(proteinGrams / 4, fatGrams / 4, carbGrams / 4)
        val dinner = createMeal(proteinGrams / 4, fatGrams / 4, carbGrams / 4)

        // 4. Adicionar as refeições ao estado (agora uma lista de listas)
        meal.clear()
        meal.addAll(listOf(morningSnack, lunch, afternoonSnack, dinner))

        // 5. Adicionar totais de macronutrientes
        totals["calories"] = dailyCalories
        totals["protein"] = proteinGrams
        totals["fat"] = fatGrams
        totals["carbs"] = carbGrams
    }

    // Função para criar uma refeição com alimentos selecionados
    fun createMeal(protein: Double, fat: Double, carbs: Double): List<String> {
        val selectedProteins = selectFoods(foodDatabase.filter { it.protein > 10 }, protein, "protein")
        val selectedFats = selectFoods(foodDatabase.filter { it.fat > 5 }, fat, "fat")
        val selectedCarbs = selectFoods(foodDatabase.filter { it.carbs > 10 }, carbs, "carb")

        return selectedProteins + selectedFats + selectedCarbs
    }

    // Função para selecionar alimentos com base na quantidade de macronutrientes necessária
    private fun selectFoods(foods: List<Food>, requiredGrams: Double, macronutrientType: String): List<String> {
        val meal = mutableListOf<String>()
        var remainingGrams = requiredGrams

        for (food in foods) {
            if (remainingGrams <= 0) break
            // A porção será baseada no tipo de macronutriente
            val portion = (remainingGrams / when(macronutrientType) {
                "protein" -> food.protein
                "fat" -> food.fat
                "carb" -> food.carbs
                else -> 1.0
            }).coerceAtMost(100.0) // Limitar a porção a 100g por alimento

            remainingGrams -= when(macronutrientType) {
                "protein" -> food.protein * (portion / 100)
                "fat" -> food.fat * (portion / 100)
                "carb" -> food.carbs * (portion / 100)
                else -> 0.0
            }

            // Exibir apenas o nome do alimento e a quantidade em gramas
            meal.add("${food.name} - ${portion.toInt()}g")
        }

        return meal
    }

}