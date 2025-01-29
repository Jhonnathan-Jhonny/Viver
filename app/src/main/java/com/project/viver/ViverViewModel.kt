@file:OptIn(InternalAPI::class)

package com.project.viver
import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.snapshots.SnapshotStateList
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

    private val _mealPlans = mutableStateListOf<MealPlan>()
    val mealPlans: SnapshotStateList<MealPlan> get() = _mealPlans

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

    val meal = mutableStateListOf<List<String>>()  // Alterando para uma lista de listas
    val totals = mutableStateMapOf<String, Double>()
    private val usedFoods = mutableSetOf<String>()


    // Alimentos para lanches (manhã e tarde) divididos por categorias
    private val snackProteins = listOf(
        Food(name = "Iogurte natural", calories = 59, protein = 3.5, fat = 3.3, carbs = 4.7),
        Food(name = "Queijo cottage", calories = 98, protein = 11.1, fat = 4.3, carbs = 3.4),
        Food(name = "Ovo cozido", calories = 68, protein = 5.5, fat = 4.8, carbs = 0.6),
        Food(name = "Peito de peru", calories = 104, protein = 18.0, fat = 1.0, carbs = 1.5),
        Food(name = "Atum em água", calories = 128, protein = 26.0, fat = 1.0, carbs = 0.0),
        Food(name = "Edamame", calories = 122, protein = 11.0, fat = 5.0, carbs = 9.0),
        Food(name = "Leite de amêndoa", calories = 30, protein = 1.0, fat = 2.5, carbs = 1.0),
        Food(name = "Proteína em pó (whey)", calories = 120, protein = 24.0, fat = 1.0, carbs = 3.0)
    )

    private val snackCarbs = listOf(
        Food(name = "Banana", calories = 89, protein = 1.1, fat = 0.3, carbs = 23.0),
        Food(name = "Maçã", calories = 52, protein = 0.3, fat = 0.2, carbs = 14.0),
        Food(name = "Pão integral", calories = 69, protein = 2.7, fat = 1.1, carbs = 11.6),
        Food(name = "Aveia", calories = 389, protein = 16.9, fat = 6.9, carbs = 66.3),
        Food(name = "Granola", calories = 471, protein = 10.0, fat = 20.0, carbs = 64.0),
        Food(name = "Manga", calories = 60, protein = 0.8, fat = 0.4, carbs = 15.0),
        Food(name = "Uvas", calories = 69, protein = 0.7, fat = 0.2, carbs = 18.0),
        Food(name = "Torrada integral", calories = 75, protein = 3.0, fat = 1.0, carbs = 12.0)
    )

    private val snackFats = listOf(
        Food(name = "Castanhas", calories = 607, protein = 15.0, fat = 55.0, carbs = 20.0),
        Food(name = "Amêndoas", calories = 576, protein = 21.1, fat = 49.9, carbs = 21.6),
        Food(name = "Pasta de amendoim", calories = 588, protein = 25.1, fat = 50.4, carbs = 20.1),
        Food(name = "Nozes", calories = 654, protein = 15.0, fat = 65.0, carbs = 14.0),
        Food(name = "Sementes de girassol", calories = 584, protein = 21.0, fat = 51.0, carbs = 20.0),
        Food(name = "Coco ralado", calories = 354, protein = 3.3, fat = 33.5, carbs = 15.0),
        Food(name = "Azeitonas", calories = 115, protein = 0.8, fat = 11.0, carbs = 6.0),
        Food(name = "Manteiga de amêndoa", calories = 614, protein = 21.0, fat = 56.0, carbs = 19.0)
    )

    // Alimentos para refeições principais (almoço e jantar) divididos por categorias
    private val mainProteins = listOf(
        Food(name = "Frango grelhado", calories = 165, protein = 31.0, fat = 3.6, carbs = 0.0),
        Food(name = "Peixe assado", calories = 206, protein = 22.0, fat = 12.0, carbs = 0.0),
        Food(name = "Carne bovina magra", calories = 250, protein = 26.0, fat = 15.0, carbs = 0.0),
        Food(name = "Tofu", calories = 76, protein = 8.0, fat = 4.8, carbs = 1.9),
        Food(name = "Salmão", calories = 208, protein = 20.0, fat = 13.0, carbs = 0.0),
        Food(name = "Camarão", calories = 99, protein = 24.0, fat = 0.3, carbs = 0.2),
        Food(name = "Lentilhas", calories = 116, protein = 9.0, fat = 0.4, carbs = 20.0),
        Food(name = "Omelete de claras", calories = 52, protein = 11.0, fat = 0.2, carbs = 0.7)
    )

    private val mainCarbs = listOf(
        Food(name = "Arroz integral", calories = 112, protein = 2.6, fat = 0.9, carbs = 23.0),
        Food(name = "Batata-doce", calories = 86, protein = 1.6, fat = 0.1, carbs = 20.0),
        Food(name = "Quinoa", calories = 120, protein = 4.1, fat = 1.9, carbs = 21.3),
        Food(name = "Feijão preto", calories = 91, protein = 5.5, fat = 0.5, carbs = 16.0),
        Food(name = "Massa integral", calories = 131, protein = 5.0, fat = 1.0, carbs = 25.0),
        Food(name = "Cuscuz", calories = 112, protein = 3.8, fat = 0.2, carbs = 23.0),
        Food(name = "Abóbora assada", calories = 49, protein = 1.0, fat = 0.2, carbs = 12.0),
        Food(name = "Grão-de-bico", calories = 164, protein = 8.9, fat = 2.6, carbs = 27.0)
    )

    private val mainFats = listOf(
        Food(name = "Azeite de oliva", calories = 119, protein = 0.0, fat = 13.5, carbs = 0.0),
        Food(name = "Abacate", calories = 160, protein = 2.0, fat = 15.0, carbs = 9.0),
        Food(name = "Sementes de chia", calories = 486, protein = 16.5, fat = 30.7, carbs = 42.1),
        Food(name = "Manteiga ghee", calories = 112, protein = 0.0, fat = 12.7, carbs = 0.0),
        Food(name = "Óleo de coco", calories = 121, protein = 0.0, fat = 13.5, carbs = 0.0),
        Food(name = "Azeitonas pretas", calories = 145, protein = 1.0, fat = 15.0, carbs = 4.0),
        Food(name = "Salmão (gordura natural)", calories = 208, protein = 20.0, fat = 13.0, carbs = 0.0),
        Food(name = "Queijo feta", calories = 264, protein = 14.0, fat = 21.0, carbs = 4.0)
    )

    // Função para criar refeições com alimentos diversificados
    private fun createMeal(protein: Double, fat: Double, carbs: Double, isSnack: Boolean): List<String> {
        // Escolher o banco de dados correto
        val proteins = if (isSnack) snackProteins else mainProteins
        val carbsList = if (isSnack) snackCarbs else mainCarbs
        val fats = if (isSnack) snackFats else mainFats

        val selectedProteins = selectFood(proteins, protein, "protein") ?: "Sem proteína"
        val selectedFats = selectFood(fats, fat, "fat") ?: "Sem gordura"

        // Selecionar carboidratos
        val selectedCarbs = if (isSnack) {
            // Para lanches, selecionar apenas um carboidrato
            listOf(selectFood(carbsList, carbs, "carb") ?: "Sem carboidrato")
        } else {
            // Para refeições principais, selecionar dois carboidratos
            val carb1 = selectFood(carbsList, carbs / 2, "carb") ?: "Sem carboidrato 1"
            val carb2 = selectFood(carbsList, carbs / 2, "carb") ?: "Sem carboidrato 2"
            listOf(carb1, carb2)
        }

        // Retornar a lista de alimentos selecionados
        return listOf(selectedProteins) + selectedCarbs + listOf(selectedFats)
    }

    // Função para selecionar um alimento do grupo
    private fun selectFood(foods: List<Food>, requiredGrams: Double, macronutrientType: String): String? {
        val availableFoods = foods.filter { it.name !in usedFoods }
        val food = availableFoods.shuffled().firstOrNull() ?: return null

        val nutrientValue = when (macronutrientType) {
            "protein" -> food.protein
            "fat" -> food.fat
            "carb" -> food.carbs
            else -> 1.0
        }

        if (nutrientValue == 0.0) {
            return null // Evitar divisão por zero
        }

        val portion = (requiredGrams / nutrientValue) * 100
        val adjustedGrams = portion.coerceAtLeast(30.0).coerceAtMost(150.0)

        usedFoods.add(food.name)
        return "${food.name} - ${adjustedGrams.toInt()}g"
    }

    // Gerar o plano alimentar com lógica atualizada
    fun generateMealPlan(weight: Double, activityLevel: Double, calorieReduction: Int = 500) {
        usedFoods.clear() // Limpar alimentos usados

        val dailyCalories = (weight * 22 * activityLevel) - calorieReduction
        val proteinGrams = weight * 2
        val fatGrams = weight * 0.8
        val carbGrams = (dailyCalories - (proteinGrams * 4 + fatGrams * 9)) / 4

        // Divisão em lanches e refeições principais
        val morningSnack = createMeal(proteinGrams / 10, fatGrams / 10, carbGrams / 5, isSnack = true)
        val lunch = createMeal(proteinGrams / 3, fatGrams / 3, carbGrams / 3, isSnack = false)
        val afternoonSnack = createMeal(proteinGrams / 10, fatGrams / 10, carbGrams / 5, isSnack = true)
        val dinner = createMeal(proteinGrams / 3, fatGrams / 3, carbGrams / 3, isSnack = false)

        // Atualizar as variáveis globais
        meal.clear()
        meal.addAll(listOf(morningSnack, lunch, afternoonSnack, dinner))

        totals["calories"] = dailyCalories
        totals["protein"] = proteinGrams
        totals["fat"] = fatGrams
        totals["carbs"] = carbGrams
    }

    fun saveMealPlanToDatabase(mealPlan: List<List<String>>, mealPlanName:String, context: Context) {
        viewModelScope.launch {
            _uiState.value = UserState.Loading
            val token = getToken(context)
            if (token.isNullOrEmpty()) {
                _uiState.value = UserState.Error("Token não encontrado.")
                Toast.makeText(context, "Token não encontrado.", Toast.LENGTH_LONG).show()
                return@launch
            }

            // Recuperando o usuário autenticado e seu email
            val user = supabase.auth.retrieveUser(token)
            if (user.id.isEmpty()) {
                _uiState.value = UserState.Error("ID do usuário não encontrado.")
                Toast.makeText(context, "ID do usuário não encontrado.", Toast.LENGTH_LONG).show()
                return@launch
            }

            try {
                supabase
                    .from("userDailyMeals")  // Nome da tabela do banco de dados
                    .insert(
                        mapOf(
                            "name_meals" to mealPlanName,
                            "user_id" to user.id,
                            "breakfast" to mealPlan[0].joinToString(","),
                            "lunch" to mealPlan[1].joinToString(","),
                            "afternoonSnack" to mealPlan[2].joinToString(","),
                            "dinner" to mealPlan[3].joinToString(",")
                        )
                    )

                    _uiState.value = UserState.Success("Plano alimentar salvo com sucesso!")
                    Toast.makeText(context, "Plano alimentar salvo com sucesso!", Toast.LENGTH_LONG).show()
            } catch (e: Exception) {
                _uiState.value = UserState.Error("Erro ao salvar o plano alimentar: ${e.message}")
                Toast.makeText(context, "Erro ao salvar o plano alimentar: ${e.message}", Toast.LENGTH_LONG).show()
                Log.e("Minha pica:", "Erro ao salvar plano alimentar", e)
            }
        }
    }

    fun fetchMealPlans(context: Context) {
        viewModelScope.launch {
            _uiState.value = UserState.Loading
            val token = getToken(context)
            if (token.isNullOrEmpty()) {
                _uiState.value = UserState.Error("Token não encontrado.")
                return@launch
            }

            val user = supabase.auth.retrieveUser(token)
            if (user.id.isEmpty()) {
                _uiState.value = UserState.Error("ID do usuário não encontrado.")
                return@launch
            }

            try {
                val mealPlans = supabase
                    .from("userDailyMeals")
                    .select(){
                        filter {
                            eq("user_id", user.id)
                        }
                    }
                    .decodeList<MealPlan>()

                _mealPlans.clear()
                _mealPlans.addAll(mealPlans)
                _uiState.value = UserState.Success("Planos alimentares carregados com sucesso!")
            } catch (e: Exception) {
                _uiState.value = UserState.Error("Erro ao buscar planos alimentares: ${e.message}")
                Log.e("ViverViewModel", "Erro ao buscar planos alimentares", e)
            }
        }
    }
}